package br.com.fiap.hackaton.dto;

import java.util.List;

public record ProfissionalAgendaDto(String profissionalNome, List<SlotDto> slots) {
}

