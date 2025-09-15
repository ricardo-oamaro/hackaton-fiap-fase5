package br.com.fiap.hackaton.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
public class TipoExameResponseDto {
    private Long id;
    private String codigo;
    private String descricao;
    private Boolean urgentePorPadrao;
    private Boolean ativo;
    private Integer duracaoMinutos;
    private String observacoes;
    private String palavrasChaveAlta;
    private String palavrasChaveMedia;
    private Integer prazoDiasAlta;
    private Integer prazoDiasMedia;
    private Integer prazoDiasBaixa;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    private OffsetDateTime criadoEm;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    private OffsetDateTime atualizadoEm;
}