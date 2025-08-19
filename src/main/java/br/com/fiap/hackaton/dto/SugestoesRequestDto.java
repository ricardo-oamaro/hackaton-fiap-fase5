package br.com.fiap.hackaton.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SugestoesRequestDto {
    @NotNull
    private Long pacienteId;
    @NotBlank
    private String tipoConsulta;
    @NotBlank private String sintomas;
    @NotNull private Double latitude;
    @NotNull private Double longitude;
    private Double raioKm;
    private int qtd = 3;
    private boolean diasUnicos = true;
    private int janelaDias = 10;
}
