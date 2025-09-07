package br.com.fiap.hackaton.service;

import br.com.fiap.hackaton.dto.ConsultaResumoDto;
import br.com.fiap.hackaton.enums.ConsultaStatus;
import br.com.fiap.hackaton.model.Consulta;
import br.com.fiap.hackaton.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;

    @Transactional(readOnly = true)
    public List<ConsultaResumoDto> listarAtivos(Long pacienteId) {
        return consultaRepository.findAtivosByPaciente(pacienteId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ConsultaResumoDto> listarHistorico(Long pacienteId) {
        return consultaRepository.findHistoricoByPaciente(pacienteId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ConsultaResumoDto> listarPorStatus(Long pacienteId, String status) {
        ConsultaStatus filtro = null;

        if (status != null && !status.isBlank()) {
            try {
                filtro = ConsultaStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Status inválido: " + status);
            }
        }

        return consultaRepository.findByPacienteAndStatus(pacienteId, filtro)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private ConsultaResumoDto mapToDto(Consulta c) {
        return ConsultaResumoDto.builder()
                .consultaId(c.getId())
                .especialidade(c.getProfissional().getEspecialidade().name())
                .status(c.getStatus().name())
                .inicio(c.getInicio())
                .fim(c.getFim())
                .ubsNome(c.getUbs().getNome())
                .profissionalNome(c.getProfissional().getNome())
                .build();
    }


}

