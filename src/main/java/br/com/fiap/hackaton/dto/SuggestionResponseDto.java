package br.com.fiap.hackaton.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class SuggestionResponseDto {
    private Long consultaId; // id da proposta criada

    private Long ubsId;
    private String ubsNome;

    private Long profissionalId;
    private String profissionalNome;

    private String especialidade;

    private OffsetDateTime inicio;
    private OffsetDateTime fim;

    private Double distanciaKm;

    private String prioridade;

    private OffsetDateTime expiresAt;
}
