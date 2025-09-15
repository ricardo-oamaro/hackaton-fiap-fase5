package br.com.fiap.hackaton.model;

import br.com.fiap.hackaton.enums.ConsultaStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(indexes = {
        @Index(name = "idx_exame_paciente_data", columnList = "paciente_id,dataAgendada"),
        @Index(name = "idx_exame_status", columnList = "status"),
        @Index(name = "idx_exame_expires", columnList = "expiresAt"),
        @Index(name = "idx_exame_ubs_tipo_data", columnList = "ubs_id,tipo_exame_id,dataAgendada")
})
public class Exame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Paciente paciente;

    @ManyToOne(optional = false)
    private UBS ubs;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_exame_id")
    private TipoExame tipoExame;

    @Enumerated(EnumType.STRING)
    private ConsultaStatus status;

    private OffsetDateTime dataAgendada;
    private OffsetDateTime dataLimite;

    private String observacoes;
    private String prioridadeTriagem;
    private String justificativaTriagem;

    private OffsetDateTime expiresAt;
    private OffsetDateTime criadoEm;
    private OffsetDateTime atualizadoEm;

    @PrePersist
    void onCreate() {
        criadoEm = OffsetDateTime.now();
        atualizadoEm = OffsetDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
        atualizadoEm = OffsetDateTime.now();
    }
}