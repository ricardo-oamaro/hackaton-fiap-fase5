package br.com.fiap.hackaton.controller;

import br.com.fiap.hackaton.dto.AgendaResponseDto;
import br.com.fiap.hackaton.service.AgendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/agenda")
@RequiredArgsConstructor
public class AgendaController {

    private final AgendaService agendaService;

    @GetMapping("/{especialidade}")
    public ResponseEntity<List<AgendaResponseDto>> getAgendaByEspecialidade(@PathVariable String especialidade) {
        List<AgendaResponseDto> agenda = agendaService.getAgendaByEspecialidade(especialidade);
        return ResponseEntity.ok(agenda);
    }

}
