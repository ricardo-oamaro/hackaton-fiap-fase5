package br.com.fiap.hackaton.exceptions;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class ApiErrorResponse extends Throwable {
    private OffsetDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
