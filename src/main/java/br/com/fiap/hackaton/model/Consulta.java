package br.com.fiap.hackaton.model;

import br.com.fiap.hackaton.enums.ConsultaStatus;
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
        @Index(name = "idx_consulta_paciente_inicio", columnList = "paciente_id,inicio"),
        @Index(name = "idx_consulta_status", columnList = "status")
})
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Paciente paciente;

    @ManyToOne(optional = false)
    private Profissional profissional;

    @ManyToOne(optional = false)
    private UBS ubs;

    @ManyToOne(optional = false)
    private SlotAgenda slot;

    private OffsetDateTime inicio;
    private OffsetDateTime fim;

    @Enumerated(EnumType.STRING)
    private ConsultaStatus status;

    private OffsetDateTime expiresAt;

    private String sintomas;

    private String prioridade;
}
