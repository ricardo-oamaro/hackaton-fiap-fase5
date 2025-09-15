package br.com.fiap.hackaton.repository;

import br.com.fiap.hackaton.enums.ConsultaStatus;
import br.com.fiap.hackaton.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    @Query("SELECT c FROM Consulta c WHERE c.paciente.id = :pacienteId AND c.status = 'AGENDADA' "+
            "AND ((:inicio BETWEEN c.inicio AND c.fim) OR (:fim BETWEEN c.inicio AND c.fim) OR (c.inicio BETWEEN :inicio AND :fim))")
    List<Consulta> findConflitos(@Param("pacienteId") Long pacienteId,
                                 @Param("inicio") OffsetDateTime inicio,
                                 @Param("fim") OffsetDateTime fim);

    Optional<Consulta> findByIdAndStatus(Long id, ConsultaStatus status);

    @Query("""
      select c.slot.id from Consulta c
      where c.status = 'AGENDADA'
         or (c.status = 'PROPOSTA' and c.expiresAt > CURRENT_TIMESTAMP)
    """)
    List<Long> findSlotsOcupados();

    @Query("""
        select c from Consulta c
        where c.paciente.id = :pacienteId
          and c.status = 'AGENDADA'
          and c.inicio >= CURRENT_TIMESTAMP
        order by c.inicio
    """)
    List<Consulta> findAtivosByPaciente(@Param("pacienteId") Long pacienteId);

    @Query("""
        select c from Consulta c
        where c.paciente.id = :pacienteId
        order by c.inicio desc
    """)
    List<Consulta> findHistoricoByPaciente(@Param("pacienteId") Long pacienteId);

    @Query("""
        select c from Consulta c
        where c.paciente.id = :pacienteId
          and (:status is null or c.status = :status)
        order by c.inicio desc
    """)
    List<Consulta> findByPacienteAndStatus(
            @Param("pacienteId") Long pacienteId,
            @Param("status") ConsultaStatus status
    );
}
