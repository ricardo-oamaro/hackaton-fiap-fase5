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
        name = "tipo_exame",
        indexes = {
                @Index(name = "idx_tipo_exame_codigo", columnList = "codigo", unique = true),
                @Index(name = "idx_tipo_exame_ativo", columnList = "ativo"),
                @Index(name = "idx_tipo_exame_descricao", columnList = "descricao")
        }
)
public class TipoExame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String codigo; // Ex: "HEMOGRAMA", "RAIO_X"

    @Column(nullable = false, length = 100)
    private String descricao; // Ex: "Hemograma Completo"

    @Column(nullable = false)
    private Boolean urgentePorPadrao = false;

    @Column(nullable = false)
    private Boolean ativo = true;

    private Integer duracaoMinutos; // Duraaco padrão do exame
    private String observacoes; // Observacoes sobre o tipo

    // Campos para regras de triagem
    private String palavrasChaveAlta; // CSV: "pre-operatório,cirurgia,urgente"
    private String palavrasChaveMedia; // CSV: "acompanhamento,controle"

    private Integer prazoDiasAlta = 3;
    private Integer prazoDiasMedia = 7;
    private Integer prazoDiasBaixa = 15;

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