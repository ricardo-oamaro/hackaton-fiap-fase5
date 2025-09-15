package br.com.fiap.hackaton.dto;

import java.util.List;

public record AgendaDiaDto(String data, List<ProfissionalAgendaDto> profissionais) {
}
