package br.com.fiap.hackaton.repository;

import br.com.fiap.hackaton.enums.ConsultaStatus;
import br.com.fiap.hackaton.model.Exame;
import br.com.fiap.hackaton.model.TipoExame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.OffsetDateTime;
import java.util.List;

public interface ExameRepository extends JpaRepository<Exame, Long> {

    @Query("SELECT e FROM Exame e WHERE e.paciente.id = :pacienteId AND e.status = 'AGENDADA' " +
            "AND e.dataAgendada BETWEEN :inicio AND :fim")
    List<Exame> findConflitosAgendamento(@Param("pacienteId") Long pacienteId,
                                         @Param("inicio") OffsetDateTime inicio,
                                         @Param("fim") OffsetDateTime fim);

    @Query("SELECT e FROM Exame e WHERE e.status = 'PROPOSTA' AND e.expiresAt < CURRENT_TIMESTAMP")
    List<Exame> findPropostasExpiradas();

    List<Exame> findByPacienteIdAndStatusOrderByDataAgendadaDesc(Long pacienteId, ConsultaStatus status);

    @Query("SELECT COUNT(e) FROM Exame e WHERE e.ubs.id = :ubsId AND e.tipoExame.id = :tipoId " +
            "AND e.dataAgendada BETWEEN :inicio AND :fim AND e.status IN ('AGENDADA', 'PROPOSTA')")
    Long countExamesNoPeriodo(@Param("ubsId") Long ubsId,
                              @Param("tipoId") Long tipoId,
                              @Param("inicio") OffsetDateTime inicio,
                              @Param("fim") OffsetDateTime fim);
}