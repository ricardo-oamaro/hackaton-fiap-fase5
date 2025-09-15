package br.com.fiap.hackaton.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SugestaoRequestDto {
    @NotNull
    private Long pacienteId;

    @NotBlank
    private String tipoConsulta;

    @NotBlank
    private String sintomas;

    @NotNull
    @DecimalMin("-90.0") @DecimalMax("90.0")
    private Double latitude;

    @NotNull
    @DecimalMin("-180.0") @DecimalMax("180.0")
    private Double longitude;

    // raio opcional em km
    @Positive
    private Double raioKm;
}
