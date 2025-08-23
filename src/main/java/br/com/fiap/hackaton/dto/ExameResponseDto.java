package br.com.fiap.hackaton.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ExameResponseDto {
    private Long exameId;
    private Long ubsId;
    private String ubsNome;
    private String tipoExame;
    private String status;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    private OffsetDateTime dataAgendada;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    private OffsetDateTime dataLimite;

    private Double distanciaKm;
    private String prioridadeTriagem;
    private String justificativaTriagem;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    private OffsetDateTime expiresAt;
}