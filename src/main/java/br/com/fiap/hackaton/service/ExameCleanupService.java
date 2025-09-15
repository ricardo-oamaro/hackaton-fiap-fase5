package br.com.fiap.hackaton.service;

import br.com.fiap.hackaton.enums.ConsultaStatus;
import br.com.fiap.hackaton.model.Exame;
import br.com.fiap.hackaton.repository.ExameRepository;
import br.com.fiap.hackaton.repository.SlotExameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExameCleanupService {

    private final ExameRepository exameRepository;
    private final SlotExameRepository slotExameRepository;

    @Scheduled(fixedDelayString = "${app.exame.cleanupIntervalMinutes:30}000") // 30 min default
    @Transactional
    public void limparPropostasExpiradas() {
        log.info("Iniciando limpeza de propostas de exames expiradas");

        var examesExpirados = exameRepository.findPropostasExpiradas();

        for (Exame exame : examesExpirados) {
            try {
                // Atualizar status para EXPIRADA
                exame.setStatus(ConsultaStatus.EXPIRADA);
                exameRepository.save(exame);

                // Liberar capacidade do slot usando busca mais precisa
                liberarCapacidadeSlot(exame);

                log.debug("Exame {} marcado como expirado", exame.getId());
            } catch (Exception e) {
                log.error("Erro ao processar exame expirado {}: {}", exame.getId(), e.getMessage());
            }
        }

        log.info("Limpeza concluída. {} propostas processadas", examesExpirados.size());
    }

    private void liberarCapacidadeSlot(Exame exame) {
        // Buscar slot específico que corresponde ao exame
        var dataInicio = exame.getDataAgendada();
        var dataFim = exame.getDataAgendada().plus(
                java.time.Duration.ofMinutes(exame.getTipoExame().getDuracaoMinutos()));

        var slotsDisponiveis = slotExameRepository.findSlotsNoPeriodo(
                exame.getTipoExame(),
                dataInicio.minusMinutes(5), // margem de tolerância
                dataFim.plusMinutes(5));

        slotsDisponiveis.stream()
                .filter(slot -> slot.getUbs().getId().equals(exame.getUbs().getId()))
                .filter(slot -> !slot.getDataInicio().isAfter(dataInicio) &&
                        !slot.getDataFim().isBefore(dataFim))
                .findFirst()
                .ifPresentOrElse(slot -> {
                    slot.setCapacidadeDisponivel(slot.getCapacidadeDisponivel() + 1);
                    slotExameRepository.save(slot);
                    log.debug("Liberada vaga no slot {} para UBS {}", slot.getId(), slot.getUbs().getNome());
                }, () -> {
                    log.warn("Slot não encontrado para exame expirado {} na UBS {}",
                            exame.getId(), exame.getUbs().getNome());
                });
    }
}