package br.com.fiap.hackaton.dto;

import java.time.OffsetDateTime;

public record SlotDto(Long id, OffsetDateTime inicio, OffsetDateTime fim, String status, String ubsNome) {
}

