package br.com.fiap.hackaton.service;

import br.com.fiap.hackaton.dto.*;
import br.com.fiap.hackaton.enums.ConsultaStatus;
import br.com.fiap.hackaton.exceptions.NoSuggestionException;
import br.com.fiap.hackaton.model.*;
import br.com.fiap.hackaton.repository.*;
import br.com.fiap.hackaton.util.GeoUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExameService {

    private final ExameRepository exameRepository;
    private final SlotExameRepository slotExameRepository;
    private final PacienteRepository pacienteRepository;
    private final TipoExameRepository tipoExameRepository;
    private final TriagemExameService triagemService;

    @Value("${app.exame.defaultRadiusKm:20}")
    private double defaultRadiusKm;

    @Value("${app.exame.holdMinutes:10}")
    private long holdMinutes;

    @Transactional
    public ExameResponseDto agendarExame(ExameRequestDto request) {
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        if (!paciente.isAtivo()) {
            throw new IllegalStateException("Paciente inativo");
        }

        TipoExame tipoExame = tipoExameRepository.findByCodigo(request.getTipoExame().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de exame não encontrado: " + request.getTipoExame()));

        if (!tipoExame.getAtivo()) {
            throw new IllegalArgumentException("Tipo de exame inativo");
        }

        String prioridade = triagemService.calcularPrioridadeExame(
                tipoExame, request.getObservacoes(), request.isUrgente(), request.getJustificativaUrgencia());

        String justificativa = triagemService.gerarJustificativa(
                tipoExame, prioridade, request.getObservacoes(), request.isUrgente());

        OffsetDateTime dataLimite = triagemService.calcularDataLimite(tipoExame, prioridade);
        OffsetDateTime agora = OffsetDateTime.now();
        double raioKm = request.getRaioKm() != null ? request.getRaioKm() : defaultRadiusKm;

        // Buscar slots disponíveis para o tipo de exame
        List<SlotExame> slots = slotExameRepository.findSlotsDisponiveis(tipoExame, agora);

        SlotExame melhorSlot = slots.stream()
                .filter(slot -> {
                    double distancia = GeoUtils.haversineKm(
                            request.getLatitude(), request.getLongitude(),
                            slot.getUbs().getLatitude(), slot.getUbs().getLongitude());
                    return distancia <= raioKm;
                })
                .filter(slot -> slot.getDataInicio().isBefore(dataLimite))
                .min(Comparator
                        .comparingInt((SlotExame slot) -> -triagemService.obterRankingPrioridade(prioridade))
                        .thenComparingDouble(slot -> GeoUtils.haversineKm(
                                request.getLatitude(), request.getLongitude(),
                                slot.getUbs().getLatitude(), slot.getUbs().getLongitude()))
                        .thenComparing(SlotExame::getDataInicio))
                .orElse(null);

        if (melhorSlot == null) {
            throw new NoSuggestionException("Nenhum horário disponível encontrado dentro do prazo e raio especificados");
        }

        // Verificar conflitos de agenda do paciente
        List<Exame> conflitos = exameRepository.findConflitosAgendamento(
                paciente.getId(), melhorSlot.getDataInicio(), melhorSlot.getDataFim());

        if (!conflitos.isEmpty()) {
            throw new NoSuggestionException("Paciente já possui exame agendado no período");
        }

        // Criar o exame
        Exame exame = Exame.builder()
                .paciente(paciente)
                .ubs(melhorSlot.getUbs())
                .tipoExame(tipoExame)
                .status(ConsultaStatus.PROPOSTA)
                .dataAgendada(melhorSlot.getDataInicio())
                .dataLimite(dataLimite)
                .observacoes(request.getObservacoes())
                .prioridadeTriagem(prioridade)
                .justificativaTriagem(justificativa)
                .expiresAt(agora.plusMinutes(holdMinutes))
                .build();

        exame = exameRepository.save(exame);

        // Reduzir capacidade disponível do slot
        melhorSlot.setCapacidadeDisponivel(melhorSlot.getCapacidadeDisponivel() - 1);
        slotExameRepository.save(melhorSlot);

        double distancia = GeoUtils.haversineKm(
                request.getLatitude(), request.getLongitude(),
                melhorSlot.getUbs().getLatitude(), melhorSlot.getUbs().getLongitude());

        return ExameResponseDto.builder()
                .exameId(exame.getId())
                .ubsId(melhorSlot.getUbs().getId())
                .ubsNome(melhorSlot.getUbs().getNome())
                .tipoExame(tipoExame.getDescricao())
                .status(exame.getStatus().name())
                .dataAgendada(exame.getDataAgendada())
                .dataLimite(exame.getDataLimite())
                .distanciaKm(distancia)
                .prioridadeTriagem(prioridade)
                .justificativaTriagem(justificativa)
                .expiresAt(exame.getExpiresAt())
                .build();
    }

    @Transactional
    public ExameResponseDto confirmarExame(Long exameId) {
        Exame exame = exameRepository.findById(exameId)
                .orElseThrow(() -> new EntityNotFoundException("Exame não encontrado"));

        if (exame.getStatus() != ConsultaStatus.PROPOSTA) {
            throw new IllegalStateException("Exame não está em estado de proposta");
        }

        if (exame.getExpiresAt().isBefore(OffsetDateTime.now())) {
            throw new IllegalStateException("Proposta de exame expirada");
        }

        exame.setStatus(ConsultaStatus.AGENDADA);
        exame = exameRepository.save(exame);

        return ExameResponseDto.builder()
                .exameId(exame.getId())
                .ubsId(exame.getUbs().getId())
                .ubsNome(exame.getUbs().getNome())
                .tipoExame(exame.getTipoExame().getDescricao())
                .status(exame.getStatus().name())
                .dataAgendada(exame.getDataAgendada())
                .dataLimite(exame.getDataLimite())
                .prioridadeTriagem(exame.getPrioridadeTriagem())
                .justificativaTriagem(exame.getJustificativaTriagem())
                .build();
    }

    @Transactional
    public void recusarExame(Long exameId) {
        Exame exame = exameRepository.findById(exameId)
                .orElseThrow(() -> new EntityNotFoundException("Exame não encontrado"));

        if (exame.getStatus() != ConsultaStatus.PROPOSTA) {
            throw new IllegalStateException("Exame não está em estado de proposta");
        }

        exame.setStatus(ConsultaStatus.RECUSADA);
        exameRepository.save(exame);

        // Liberar capacidade do slot
        liberarCapacidadeSlot(exame);
    }

    @Transactional(readOnly = true)
    public ExamesDisponiveisResponseDto listarExamesDisponiveis(ExamesDisponiveisRequestDto request) {
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        TipoExame tipoExame = tipoExameRepository.findByCodigo(request.getTipoExame().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de exame não encontrado"));

        OffsetDateTime agora = OffsetDateTime.now();
        OffsetDateTime fimJanela = agora.plusDays(request.getJanelaDias());
        double raioKm = request.getRaioKm() != null ? request.getRaioKm() : defaultRadiusKm;

        List<SlotExame> slots = slotExameRepository.findSlotsNoPeriodo(tipoExame, agora, fimJanela);

        List<ExameDisponivelDto> opcoes = slots.stream()
                .filter(slot -> {
                    double distancia = GeoUtils.haversineKm(
                            request.getLatitude(), request.getLongitude(),
                            slot.getUbs().getLatitude(), slot.getUbs().getLongitude());
                    return distancia <= raioKm;
                })
                .sorted(Comparator
                        .comparing(SlotExame::getDataInicio)
                        .thenComparingDouble(slot -> GeoUtils.haversineKm(
                                request.getLatitude(), request.getLongitude(),
                                slot.getUbs().getLatitude(), slot.getUbs().getLongitude())))
                .limit(request.getQuantidadeMaxima())
                .map(slot -> {
                    double distancia = GeoUtils.haversineKm(
                            request.getLatitude(), request.getLongitude(),
                            slot.getUbs().getLatitude(), slot.getUbs().getLongitude());

                    return ExameDisponivelDto.builder()
                            .slotId(slot.getId())
                            .ubsId(slot.getUbs().getId())
                            .ubsNome(slot.getUbs().getNome())
                            .tipoExame(slot.getTipoExame().getDescricao())
                            .dataInicio(slot.getDataInicio())
                            .dataFim(slot.getDataFim())
                            .vagasDisponiveis(slot.getCapacidadeDisponivel())
                            .distanciaKm(distancia)
                            .build();
                })
                .toList();

        return ExamesDisponiveisResponseDto.builder()
                .opcoes(opcoes)
                .totalEncontrados(opcoes.size())
                .build();
    }

    private void liberarCapacidadeSlot(Exame exame) {
        var dataInicio = exame.getDataAgendada();
        var dataFim = exame.getDataAgendada().plus(
                java.time.Duration.ofMinutes(exame.getTipoExame().getDuracaoMinutos()));

        // Buscar slot que contém o período do exame
        var slotsDisponiveis = slotExameRepository.findSlotsNoPeriodo(
                exame.getTipoExame(),
                dataInicio.minusMinutes(30), // margem maior
                dataFim.plusMinutes(30));

        slotsDisponiveis.stream()
                .filter(slot -> slot.getUbs().getId().equals(exame.getUbs().getId()))
                .filter(slot ->
                        // Slot deve cobrir o período do exame
                        !slot.getDataInicio().isAfter(dataInicio) &&
                                !slot.getDataFim().isBefore(dataFim))
                .findFirst()
                .ifPresentOrElse(slot -> {
                    slot.setCapacidadeDisponivel(slot.getCapacidadeDisponivel() + 1);
                    slotExameRepository.save(slot);
                }, () -> {
                    // Se não encontrar slot específico, tentar buscar por código do tipo
                    var slotsPorCodigo = slotExameRepository.findSlotsPorCodigoTipo(
                            exame.getTipoExame().getCodigo(),
                            dataInicio.minusHours(1));

                    slotsPorCodigo.stream()
                            .filter(slot -> slot.getUbs().getId().equals(exame.getUbs().getId()))
                            .filter(slot -> !slot.getDataInicio().isAfter(dataInicio) &&
                                    !slot.getDataFim().isBefore(dataFim))
                            .findFirst()
                            .ifPresent(slot -> {
                                slot.setCapacidadeDisponivel(slot.getCapacidadeDisponivel() + 1);
                                slotExameRepository.save(slot);
                            });
                });
    }
}