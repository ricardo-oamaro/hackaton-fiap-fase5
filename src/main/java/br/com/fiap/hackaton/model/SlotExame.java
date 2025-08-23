package br.com.fiap.hackaton.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "slot_exame",
        indexes = {
                @Index(name = "idx_slot_exame_ubs_tipo", columnList = "ubs_id,tipo_exame_id"),
                @Index(name = "idx_slot_exame_data", columnList = "dataInicio,dataFim"),
                @Index(name = "idx_slot_exame_disponivel", columnList = "ativo,capacidadeDisponivel,dataInicio"),
                @Index(name = "idx_slot_exame_tipo_data", columnList = "tipo_exame_id,dataInicio,ativo")
        }
)
public class SlotExame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private UBS ubs;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_exame_id")
    private TipoExame tipoExame;

    private OffsetDateTime dataInicio;
    private OffsetDateTime dataFim;

    private Integer capacidadeTotal;
    private Integer capacidadeDisponivel;

    private boolean ativo;
}