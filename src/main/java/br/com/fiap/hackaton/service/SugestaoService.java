package br.com.fiap.hackaton.service;

import br.com.fiap.hackaton.record.RankedSlot;
import br.com.fiap.hackaton.dto.SuggestionRequestDto;
import br.com.fiap.hackaton.dto.SuggestionResponseDto;
import br.com.fiap.hackaton.enums.ConsultaStatus;
import br.com.fiap.hackaton.enums.Especialidade;
import br.com.fiap.hackaton.exceptions.NoSuggestionException;
import br.com.fiap.hackaton.model.Consulta;
import br.com.fiap.hackaton.repository.ConsultaRepository;
import br.com.fiap.hackaton.repository.PacienteRepository;
import br.com.fiap.hackaton.repository.SlotAgendaRepository;
import br.com.fiap.hackaton.util.GeoUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SugestaoService {
    private final PacienteRepository pacienteRepository;
    private final SlotAgendaRepository slotAgendaRepository;
    private final ConsultaRepository consultaRepository;
    private final TriagemService triagemService;

    @Value("${app.sugestao.defaultRadiusKm:15}")
    private double defaultRadiusKm;

    @Value("${app.sugestao.holdMinutes:5}")
    private long holdMinutes;

    @Transactional
    public SuggestionResponseDto sugerir(SuggestionRequestDto req) {
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

        // Carrega slots futuros para CLINICO_GERAL
        var slots = slotAgendaRepository.findDisponiveisByEspecialidade(Especialidade.CLINICO_GERAL, agora);

        // Escolhe o melhor slot: prioridade (via triagem) -> proximidade -> data/hora
        var melhor = slots.stream()
                .map(s -> new RankedSlot(s,
                        GeoUtils.haversineKm(req.getLatitude(), req.getLongitude(),
                                s.getUbs().getLatitude(), s.getUbs().getLongitude())))
                .filter(rs -> rs.distanciaKm() <= raioKm)
                .sorted(Comparator
                        .comparingInt((RankedSlot rs) -> triagemService.prioridadeRank(prioridade)).reversed()
                        .thenComparingDouble(rs -> rs.distanciaKm())
                        .thenComparing(rs -> rs.slot().getInicio()))
                .map(rs -> rs.slot())
                .findFirst()
                .orElse(null);

        if (melhor == null) {
            throw new NoSuggestionException("Nenhum horário disponível dentro do raio");
        }

        // Verifica conflito de horário do paciente
        var conflitos = consultaRepository.findConflitos(paciente.getId(), melhor.getInicio(), melhor.getFim());
        if (!conflitos.isEmpty()) {
            throw new NoSuggestionException("Conflito com outra consulta do paciente");
        }

        // Cria consulta PROPOSTA (hold lógico com TTL)
        var proposta = Consulta.builder()
                .paciente(paciente)
                .profissional(melhor.getProfissional())
                .ubs(melhor.getUbs())
                .slot(melhor)
                .inicio(melhor.getInicio())
                .fim(melhor.getFim())
                .status(ConsultaStatus.PROPOSTA)
                .expiresAt(agora.plusMinutes(holdMinutes))
                .sintomas(req.getSintomas())
                .prioridade(prioridade)
                .build();
        proposta = consultaRepository.save(proposta);

        var ubs = melhor.getUbs();
        var prof = melhor.getProfissional();

        return SuggestionResponseDto.builder()
                .consultaId(proposta.getId())
                .ubsId(ubs.getId())
                .ubsNome(ubs.getNome())
                .profissionalId(prof.getId())
                .profissionalNome(prof.getNome())
                .especialidade(prof.getEspecialidade().name())
                .inicio(melhor.getInicio())
                .fim(melhor.getFim())
                .distanciaKm(GeoUtils.haversineKm(req.getLatitude(), req.getLongitude(), ubs.getLatitude(), ubs.getLongitude()))
                .prioridade(prioridade)
                .expiresAt(proposta.getExpiresAt())
                .build();
    }
}
