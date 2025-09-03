package br.com.fiap.hackaton.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class AgendaResponseDto {

    private String profissionalNome;
    private OffsetDateTime inicio;
    private OffsetDateTime fim;
    private String ubsNome;

}
