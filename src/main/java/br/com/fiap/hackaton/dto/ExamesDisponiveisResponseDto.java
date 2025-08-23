package br.com.fiap.hackaton.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ExamesDisponiveisResponseDto {
    private List<ExameDisponivelDto> opcoes;
    private int totalEncontrados;
}