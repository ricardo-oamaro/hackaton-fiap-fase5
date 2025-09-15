package br.com.fiap.hackaton.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class SugestaoResponseDto {
    private Long consultaId;

    private Long ubsId;
    private String ubsNome;

    private Long profissionalId;
    private String profissionalNome;

    private String especialidade;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private OffsetDateTime inicio;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private OffsetDateTime fim;

    private Double distanciaKm;

    private String prioridade;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private OffsetDateTime expiresAt;
}
