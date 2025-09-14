package br.com.fiap.hackaton.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExameRequestDto {
    @NotNull(message = "ID do paciente é obrigatório")
    @Positive(message = "ID do paciente deve ser positivo")
    private Long pacienteId;

    @NotBlank(message = "Tipo de exame é obrigatório")
    @Size(min = 2, max = 50, message = "Tipo de exame deve ter entre 2 e 50 caracteres")
    private String tipoExame;

    @NotBlank(message = "Observações são obrigatórias")
    @Size(min = 10, max = 500, message = "Observações devem ter entre 10 e 500 caracteres")
    private String observacoes;

    @NotNull(message = "Latitude é obrigatória")
    @DecimalMin(value = "-90.0", message = "Latitude deve ser >= -90")
    @DecimalMax(value = "90.0", message = "Latitude deve ser <= 90")
    private Double latitude;

    @NotNull(message = "Longitude é obrigatória")
    @DecimalMin(value = "-180.0", message = "Longitude deve ser >= -180")
    @DecimalMax(value = "180.0", message = "Longitude deve ser <= 180")
    private Double longitude;

    @Positive(message = "Raio deve ser positivo")
    @DecimalMax(value = "100.0", message = "Raio máximo é 100km")
    private Double raioKm;

    private boolean urgente = false;

    @Size(max = 200, message = "Justificativa deve ter no máximo 200 caracteres")
    private String justificativaUrgencia;

    // Validacao customizada: se urgente=true, justificativa e obrigatoria
    @AssertTrue(message = "Justificativa é obrigatória quando exame é marcado como urgente")
    public boolean isJustificativaValida() {
        if (urgente) {
            return justificativaUrgencia != null && !justificativaUrgencia.trim().isEmpty();
        }
        return true;
    }
}