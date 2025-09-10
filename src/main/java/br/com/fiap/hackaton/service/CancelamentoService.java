package br.com.fiap.hackaton.service;

import br.com.fiap.hackaton.dto.CancelamentoResponseDto;
import br.com.fiap.hackaton.enums.ConsultaStatus;
import br.com.fiap.hackaton.repository.ConsultaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class CancelamentoService {

    private final ConsultaRepository consultaRepository;

    @Transactional
    public CancelamentoResponseDto cancelar(Long consultaId) {
        var consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new EntityNotFoundException("Consulta não encontrada"));

        if (consulta.getStatus() != ConsultaStatus.AGENDADA) {
            throw new IllegalStateException("Consulta não está em estado AGENDADA");
        }

        if (consulta.getExpiresAt().isBefore(OffsetDateTime.now())) {
            throw new IllegalStateException("Consulta expirada");
        }

        // Atualiza status para CANCELADA
        consulta.setStatus(ConsultaStatus.CANCELADA);
        consulta = consultaRepository.save(consulta);

        return CancelamentoResponseDto.builder()
                .consultaId(consulta.getId())
                .status(consulta.getStatus().name())
                .inicio(consulta.getInicio())
                .fim(consulta.getFim())
                .ubsNome(consulta.getUbs().getNome())
                .profissionalNome(consulta.getProfissional().getNome())
                .build();
    }
}
