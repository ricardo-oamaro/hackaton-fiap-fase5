package br.com.fiap.hackaton.config;

import br.com.fiap.hackaton.dto.ErrorResponseDto;
import br.com.fiap.hackaton.exceptions.NoSuggestionException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        var msg = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .findFirst()
                .orElse("Payload inválido");

        log.warn("Validation error on {}: {}", request.getRequestURI(), msg);

        return ResponseEntity.badRequest().body(
                ErrorResponseDto.builder()
                        .code("VALIDATION_ERROR")
                        .message(msg)
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        log.warn("Entity not found on {}: {}", request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResponseDto.builder()
                        .code("NOT_FOUND")
                        .message(ex.getMessage())
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalState(IllegalStateException ex, HttpServletRequest request) {
        log.warn("Illegal state on {}: {}", request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ErrorResponseDto.builder()
                        .code("CONFLICT")
                        .message(ex.getMessage())
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }

    @ExceptionHandler(NoSuggestionException.class)
    public ResponseEntity<ErrorResponseDto> handleNoSuggestion(NoSuggestionException ex, HttpServletRequest request) {
        log.warn("No suggestion on {}: {}", request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                ErrorResponseDto.builder()
                        .code("NO_SUGGESTION")
                        .message(ex.getMessage())
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("Illegal argument on {}: {}", request.getRequestURI(), ex.getMessage());

        return ResponseEntity.badRequest().body(
                ErrorResponseDto.builder()
                        .code("BAD_REQUEST")
                        .message(ex.getMessage())
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error on {}: {}", request.getRequestURI(), ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ErrorResponseDto.builder()
                        .code("INTERNAL_ERROR")
                        .message("Erro inesperado no servidor")
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }
}