package br.com.fiap.hackaton.controller;

import br.com.fiap.hackaton.dto.ConsultaResumoDto;
import br.com.fiap.hackaton.service.ConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;

    // Consultas ativas (futuras e em andamento)
    @GetMapping("/ativos/{pacienteId}")
    public List<ConsultaResumoDto> listarAtivos(@PathVariable Long pacienteId) {
        return consultaService.listarAtivos(pacienteId);
    }

    // Histórico de consultas (passadas)
    @GetMapping("/historico/{pacienteId}")
    public List<ConsultaResumoDto> listarHistorico(@PathVariable Long pacienteId) {
        return consultaService.listarHistorico(pacienteId);
    }

    @GetMapping("/{pacienteId}")
    public List<ConsultaResumoDto> listar(
            @PathVariable Long pacienteId,
            @RequestParam(required = false) String status
    ) {
        return consultaService.listarPorStatus(pacienteId, status);
    }
}

