package br.com.fiap.hackaton.service;

import br.com.fiap.hackaton.dto.SugestoesItemDto;
import br.com.fiap.hackaton.dto.SugestoesRequestDto;
import br.com.fiap.hackaton.dto.SugestoesResponseDto;
import br.com.fiap.hackaton.enums.Especialidade;
import br.com.fiap.hackaton.model.SlotAgenda;
import br.com.fiap.hackaton.repository.ConsultaRepository;
import br.com.fiap.hackaton.repository.PacienteRepository;
import br.com.fiap.hackaton.repository.SlotAgendaRepository;
import br.com.fiap.hackaton.util.GeoUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SugestoesService {
    private final PacienteRepository pacienteRepository;
    private final SlotAgendaRepository slotAgendaRepository;
    private final ConsultaRepository consultaRepository;
    private final TriagemService triagemService;

    @Value("${app.sugestao.defaultRadiusKm:15}")
    private double defaultRadiusKm;

    @Transactional(readOnly = true)
    public SugestoesResponseDto listar(SugestoesRequestDto req) {
        var paciente = pacienteRepository.findById(req.getPacienteId())
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        if (!paciente.isAtivo()) {
            throw new IllegalStateException("Paciente inativo");
        }
        if (!"clinico_geral".equalsIgnoreCase(req.getTipoConsulta())) {
            throw new IllegalArgumentException("Tipo de consulta não suportado no MVP");
        }

        var prioridade = triagemService.calcularPrioridade(req.getSintomas());
        var agora = OffsetDateTime.now();
        var raioKm = Optional.ofNullable(req.getRaioKm()).orElse(defaultRadiusKm);

        // Janela de busca: agora até agora + janelaDias
        var fimJanela = agora.plusDays(Math.max(req.getJanelaDias(), 1));

        // Slots disponíveis no período
        var slots = slotAgendaRepository.findDisponiveisByEspecialidade(Especialidade.CLINICO_GERAL, agora)
                .stream()
                .filter(s -> !s.getInicio().isAfter(fimJanela))
                .toList();

        // Mapear slots com distância e filtrar
        var candidatos = slots.stream()
                .map(s -> new RankedSlot(s, distancia(req.getLatitude(), req.getLongitude(), s)))
                .filter(rs -> rs.distanciaKm <= raioKm)
                .filter(rs -> consultaRepository.findConflitos(
                        paciente.getId(), rs.slot.getInicio(), rs.slot.getFim()
                ).isEmpty())
                .collect(Collectors.toList());

        // Ordenação
        candidatos.sort(Comparator
                .comparingInt((RankedSlot rs) -> triagemService.prioridadeRank(prioridade)).reversed()
                .thenComparingDouble(rs -> rs.distanciaKm)
                .thenComparing(rs -> rs.slot.getInicio())
        );

        // Se diasUnicos: pegar só 1 slot por dia
        List<RankedSlot> selecionados;
        if (req.isDiasUnicos()) {
            var zone = ZoneId.of("America/Sao_Paulo");
            Map<LocalDate, RankedSlot> porDia = new LinkedHashMap<>();
            for (var rs : candidatos) {
                var dia = rs.slot.getInicio().atZoneSameInstant(zone).toLocalDate();
                porDia.putIfAbsent(dia, rs); // pega só o primeiro (já ordenado)
            }
            selecionados = new ArrayList<>(porDia.values());
        } else {
            selecionados = candidatos;
        }

        // Limitar ao número pedido
        int limite = Math.max(req.getQtd(), 1);
        if (selecionados.size() > limite) {
            selecionados = selecionados.subList(0, limite);
        }

        // Mapear para DTO
        var itens = selecionados.stream().map(rs -> {
            var ubs = rs.slot.getUbs();
            var prof = rs.slot.getProfissional();
            return SugestoesItemDto.builder()
                    .slotId(rs.slot.getId())
                    .ubsId(ubs.getId())
                    .ubsNome(ubs.getNome())
                    .profissionalId(prof.getId())
                    .profissionalNome(prof.getNome())
                    .especialidade(prof.getEspecialidade().name())
                    .inicio(rs.slot.getInicio())
                    .fim(rs.slot.getFim())
                    .distanciaKm(rs.distanciaKm)
                    .prioridade(prioridade)
                    .build();
        }).toList();

        return SugestoesResponseDto.builder()
                .opcoes(itens)
                .build();
    }

    private static double distancia(double lat, double lon, SlotAgenda s) {
        return GeoUtils.haversineKm(lat, lon, s.getUbs().getLatitude(), s.getUbs().getLongitude());
    }

    private record RankedSlot(SlotAgenda slot, double distanciaKm) {}
}
