package br.com.fiap.hackaton.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class ErrorResponseDto {

    private String code;
    private String message;
    private OffsetDateTime timestamp;
}
