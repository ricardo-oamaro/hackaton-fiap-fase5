package br.com.fiap.hackaton.service;

import br.com.fiap.hackaton.dto.AgendamentoResponseDto;
import br.com.fiap.hackaton.enums.ConsultaStatus;
import br.com.fiap.hackaton.repository.ConsultaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final ConsultaRepository consultaRepository;

    @Transactional
    public AgendamentoResponseDto confirmar(Long consultaId) {
        var consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new EntityNotFoundException("Consulta não encontrada"));

        if (consulta.getStatus() != ConsultaStatus.PROPOSTA) {
            throw new IllegalStateException("Consulta não está em estado PROPOSTA");
        }

        if (consulta.getExpiresAt().isBefore(OffsetDateTime.now())) {
            throw new IllegalStateException("Proposta expirada");
        }

        // Atualiza status para AGENDADA
        consulta.setStatus(ConsultaStatus.AGENDADA);
        consulta = consultaRepository.save(consulta);

        return AgendamentoResponseDto.builder()
                .consultaId(consulta.getId())
                .status(consulta.getStatus().name())
                .inicio(consulta.getInicio())
                .fim(consulta.getFim())
                .ubsNome(consulta.getUbs().getNome())
                .profissionalNome(consulta.getProfissional().getNome())
                .build();
    }
}
