package br.com.fiap.hackaton.controller;

import br.com.fiap.hackaton.dto.*;
import br.com.fiap.hackaton.service.AgendamentoService;
import br.com.fiap.hackaton.service.SugestaoService;
import br.com.fiap.hackaton.service.SugestoesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agendamentos")
@RequiredArgsConstructor
public class AgendamentosController {

    private final SugestaoService sugestaoService;
    private final SugestoesService sugestoesService;
    private final AgendamentoService agendamentoService;

    @PostMapping("/sugestao")
    public ResponseEntity<SugestaoResponseDto> sugerir(@Valid @RequestBody SugestaoRequestDto req) {
        var resp = sugestaoService.sugerir(req);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/sugestoes")
    public SugestoesResponseDto listarSugestoes(@Valid @RequestBody SugestoesRequestDto req) {
        return sugestoesService.listar(req);
    }

    @PostMapping("/{id}/confirmar")
    public ResponseEntity<AgendamentoResponseDto> confirmar(@PathVariable Long id) {
        var dto = agendamentoService.confirmar(id);
        return ResponseEntity.ok(dto);
    }
}