package br.com.fiap.hackaton.service;

import br.com.fiap.hackaton.dto.AgendamentoResponseDto;
import br.com.fiap.hackaton.enums.ConsultaStatus;
import br.com.fiap.hackaton.model.Consulta;
import br.com.fiap.hackaton.repository.ConsultaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public AgendamentoResponseDto recusar(Long consultaId) {
        var consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new EntityNotFoundException("Consulta não encontrada"));

        if (consulta.getStatus() != ConsultaStatus.PROPOSTA) {
            throw new IllegalStateException("Só propostas podem ser recusadas");
        }

        // Atualiza status para RECUSADA
        consulta.setStatus(ConsultaStatus.RECUSADA);
        consulta = consultaRepository.save(consulta);

        return AgendamentoResponseDto.builder()
                .consultaId(consulta.getId())
                .status("Recusada") // resposta amigável
                .inicio(consulta.getInicio())
                .fim(consulta.getFim())
                .ubsNome(consulta.getUbs().getNome())
                .profissionalNome(consulta.getProfissional().getNome())
                .build();
    }

    @Transactional
    public AgendamentoResponseDto cancelar(Long consultaId) {
        var consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new EntityNotFoundException("Consulta não encontrada"));

        if (consulta.getStatus() != ConsultaStatus.AGENDADA) {
            throw new IllegalStateException("Só consultas AGENDADAS podem ser canceladas");
        }

        consulta.setStatus(ConsultaStatus.CANCELADA);
        consulta = consultaRepository.save(consulta);

        return AgendamentoResponseDto.builder()
                .consultaId(consulta.getId())
                .status("Cancelada")
                .inicio(consulta.getInicio())
                .fim(consulta.getFim())
                .ubsNome(consulta.getUbs().getNome())
                .profissionalNome(consulta.getProfissional().getNome())
                .build();
    }
    
    public List<AgendamentoResponseDto> buscarConsultasAtivas(Long pacienteId) {
        List<Consulta> consultasAtivas = consultaRepository.findByPacienteIdAndStatusIn(
            pacienteId,
            List.of(ConsultaStatus.PROPOSTA, ConsultaStatus.AGENDADA)
        );
        return consultasAtivas.stream()
            .map(consulta -> AgendamentoResponseDto.builder()
                .consultaId(consulta.getId())
                .status(consulta.getStatus().name())
                .inicio(consulta.getInicio())
                .fim(consulta.getFim())
                .ubsNome(consulta.getUbs().getNome())
                .profissionalNome(consulta.getProfissional().getNome())
                .build())
            .collect(Collectors.toList());
    }

    public List<AgendamentoResponseDto> buscarHistoricoConsultas(Long pacienteId) {
        List<Consulta> historicoConsultas = consultaRepository.findByPacienteIdAndStatusInOrderByInicioDesc(
            pacienteId,
            List.of(ConsultaStatus.RECUSADA, ConsultaStatus.EXPIRADA,
                   ConsultaStatus.CANCELADA, ConsultaStatus.REALIZADA)
        );
        return historicoConsultas.stream()
            .map(consulta -> AgendamentoResponseDto.builder()
                .consultaId(consulta.getId())
                .status(consulta.getStatus().name())
                .inicio(consulta.getInicio())
                .fim(consulta.getFim())
                .ubsNome(consulta.getUbs().getNome())
                .profissionalNome(consulta.getProfissional().getNome())
                .build())
            .collect(Collectors.toList());
    }
}