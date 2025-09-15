package br.com.fiap.hackaton.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class ConsultaResumoDto {
    private Long consultaId;
    private String especialidade;
    private String status;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    private OffsetDateTime inicio;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    private OffsetDateTime fim;

    private String ubsNome;
    private String profissionalNome;
}
