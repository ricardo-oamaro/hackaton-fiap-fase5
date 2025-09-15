package br.com.fiap.hackaton.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ExameDisponivelDto {
    private Long slotId;
    private Long ubsId;
    private String ubsNome;
    private String tipoExame;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    private OffsetDateTime dataInicio;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    private OffsetDateTime dataFim;

    private Integer vagasDisponiveis;
    private Double distanciaKm;
}