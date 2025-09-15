package br.com.fiap.hackaton.controller;

import br.com.fiap.hackaton.dto.*;
import br.com.fiap.hackaton.exceptions.ApiErrorResponse;
import br.com.fiap.hackaton.exceptions.NoSuggestionException;
import br.com.fiap.hackaton.service.AgendamentoService;
import br.com.fiap.hackaton.service.SugestaoService;
import br.com.fiap.hackaton.service.SugestoesService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        var msg = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .findFirst().orElse("Payload inválido");
        return ResponseEntity.badRequest().body(err("BAD_REQUEST", msg));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err("NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err("FORBIDDEN", ex.getMessage()));
    }

    @ExceptionHandler(NoSuggestionException.class)
    public ResponseEntity<ErrorResponse> handleNoSuggestion(NoSuggestionException ex) {
        return ResponseEntity.unprocessableEntity().body(err("NO_SUGGESTION", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArg(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(err("BAD_REQUEST", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err("INTERNAL_ERROR", "Erro inesperado"));
    }

    private ErrorResponse err(String error, String message) {
        var body = ApiErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(0)
                .error(error)
                .message(message)
                .path("/agendamentos/sugestao")
                .build();
        return ErrorResponse.create(body, HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
}
