package br.com.fiap.hackaton.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExamesDisponiveisRequestDto {
    @NotNull(message = "ID do paciente é obrigatório")
    private Long pacienteId;

    @NotBlank(message = "Tipo de exame é obrigatório")
    private String tipoExame;

    @NotNull(message = "Latitude é obrigatória")
    private Double latitude;

    @NotNull(message = "Longitude é obrigatória")
    private Double longitude;

    @Positive(message = "Raio deve ser positivo")
    private Double raioKm;

    @Min(value = 1, message = "Quantidade máxima deve ser ao menos 1")
    private int quantidadeMaxima = 5;

    @Min(value = 1, message = "Janela de dias deve ser ao menos 1")
    private int janelaDias = 15;
}