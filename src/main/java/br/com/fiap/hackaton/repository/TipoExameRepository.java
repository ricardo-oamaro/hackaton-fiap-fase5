package br.com.fiap.hackaton.repository;

import br.com.fiap.hackaton.model.TipoExame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface TipoExameRepository extends JpaRepository<TipoExame, Long> {

    Optional<TipoExame> findByCodigo(String codigo);

    List<TipoExame> findByAtivoTrueOrderByDescricao();

    @Query("SELECT t FROM TipoExame t WHERE t.ativo = true AND " +
            "(UPPER(t.codigo) LIKE UPPER(CONCAT('%', ?1, '%')) OR " +
            "UPPER(t.descricao) LIKE UPPER(CONCAT('%', ?1, '%')))")
    List<TipoExame> buscarPorTexto(String texto);

    boolean existsByCodigo(String codigo);
}