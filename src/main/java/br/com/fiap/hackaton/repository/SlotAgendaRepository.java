package br.com.fiap.hackaton.repository;

import br.com.fiap.hackaton.enums.Especialidade;
import br.com.fiap.hackaton.model.SlotAgenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface SlotAgendaRepository extends JpaRepository<SlotAgenda, Long> {
    @Query("SELECT s FROM SlotAgenda s WHERE s.profissional.especialidade = :esp AND s.inicio >= :inicio")
    List<SlotAgenda> findDisponiveisByEspecialidade(@Param("esp") Especialidade esp,
                                                    @Param("inicio") OffsetDateTime inicio);

    @Query("SELECT s FROM SlotAgenda s WHERE s.profissional.especialidade = :esp AND s.inicio BETWEEN :start AND :end")
    List<SlotAgenda> findByEspecialidadeAndDate(@Param("esp") Especialidade esp,
                                                @Param("start") OffsetDateTime start,
                                                @Param("end") OffsetDateTime end);
}
