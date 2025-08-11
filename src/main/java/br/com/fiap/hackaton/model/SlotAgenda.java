package br.com.fiap.hackaton.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(indexes = {
        @Index(name = "idx_slot_profissional_inicio", columnList = "profissional_id,inicio"),
        @Index(name = "idx_slot_ubs_inicio", columnList = "ubs_id,inicio")
})
public class SlotAgenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Profissional profissional;

    @ManyToOne(optional = false)
    private UBS ubs;

    private OffsetDateTime inicio;
    private OffsetDateTime fim;

    // tipo de atendimento simplificado (igual à especialidade principal do profissional no MVP)
}
