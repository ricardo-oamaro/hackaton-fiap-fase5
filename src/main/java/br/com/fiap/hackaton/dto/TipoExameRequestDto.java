package br.com.fiap.hackaton.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TipoExameRequestDto {
    @NotBlank(message = "Código é obrigatório")
    @Size(max = 50, message = "Código deve ter no máximo 50 caracteres")
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "Código deve conter apenas letras maiúsculas, números e underscore")
    private String codigo;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 100, message = "Descrição deve ter no máximo 100 caracteres")
    private String descricao;

    private Boolean urgentePorPadrao = false;

    @Positive(message = "Duração deve ser positiva")
    private Integer duracaoMinutos;

    @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
    private String observacoes;

    private String palavrasChaveAlta;
    private String palavrasChaveMedia;

    @Min(value = 1, message = "Prazo para alta prioridade deve ser ao menos 1 dia")
    private Integer prazoDiasAlta = 3;

    @Min(value = 1, message = "Prazo para média prioridade deve ser ao menos 1 dia")
    private Integer prazoDiasMedia = 7;

    @Min(value = 1, message = "Prazo para baixa prioridade deve ser ao menos 1 dia")
    private Integer prazoDiasBaixa = 15;
}