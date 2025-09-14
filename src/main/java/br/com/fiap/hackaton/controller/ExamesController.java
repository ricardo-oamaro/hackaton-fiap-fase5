package br.com.fiap.hackaton.controller;

import br.com.fiap.hackaton.dto.*;
import br.com.fiap.hackaton.service.ExameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exames")
@RequiredArgsConstructor
public class ExamesController {

    private final ExameService exameService;

    @PostMapping("/agendar")
    public ResponseEntity<ExameResponseDto> agendarExame(@Valid @RequestBody ExameRequestDto request) {
        var response = exameService.agendarExame(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/confirmar")
    public ResponseEntity<ExameResponseDto> confirmarExame(@PathVariable Long id) {
        var response = exameService.confirmarExame(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/recusar")
    public ResponseEntity<Void> recusarExame(@PathVariable Long id) {
        exameService.recusarExame(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reagendar")
    public ResponseEntity<ExameResponseDto> reagendarExame(@PathVariable Long id,
                                                           @Valid @RequestBody ExameRequestDto request) {
        // Recusar exame atual e criar novo
        exameService.recusarExame(id);
        var response = exameService.agendarExame(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/disponiveis")
    public ResponseEntity<ExamesDisponiveisResponseDto> listarDisponiveis(@Valid @RequestBody ExamesDisponiveisRequestDto request) {
        var response = exameService.listarExamesDisponiveis(request);
        return ResponseEntity.ok(response);
    }
}