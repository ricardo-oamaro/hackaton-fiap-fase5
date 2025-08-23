package br.com.fiap.hackaton.repository;

import br.com.fiap.hackaton.model.SlotExame;
import br.com.fiap.hackaton.model.TipoExame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface SlotExameRepository extends JpaRepository<SlotExame, Long> {

    @Query("SELECT s FROM SlotExame s WHERE s.tipoExame = :tipo AND s.ativo = true " +
            "AND s.dataInicio >= :dataMinima AND s.capacidadeDisponivel > 0 " +
            "ORDER BY s.dataInicio")
    List<SlotExame> findSlotsDisponiveis(@Param("tipo") TipoExame tipo,
                                         @Param("dataMinima") OffsetDateTime dataMinima);

    @Query("SELECT s FROM SlotExame s WHERE s.tipoExame = :tipo AND s.ativo = true " +
            "AND s.dataInicio BETWEEN :inicio AND :fim AND s.capacidadeDisponivel > 0")
    List<SlotExame> findSlotsNoPeriodo(@Param("tipo") TipoExame tipo,
                                       @Param("inicio") OffsetDateTime inicio,
                                       @Param("fim") OffsetDateTime fim);

    @Query("SELECT s FROM SlotExame s WHERE s.tipoExame.codigo = :codigoTipo AND s.ativo = true " +
            "AND s.dataInicio >= :dataMinima AND s.capacidadeDisponivel > 0 " +
            "ORDER BY s.dataInicio")
    List<SlotExame> findSlotsPorCodigoTipo(@Param("codigoTipo") String codigoTipo,
                                           @Param("dataMinima") OffsetDateTime dataMinima);

    @Query("SELECT s FROM SlotExame s WHERE s.tipoExame = :tipo AND s.ubs.id = :ubsId " +
            "AND s.ativo = true AND s.dataInicio <= :inicioDesejado " +
            "AND s.dataFim >= :fimDesejado AND s.capacidadeDisponivel > 0")
    Optional<SlotExame> findSlotCompativel(@Param("tipo") TipoExame tipo,
                                           @Param("ubsId") Long ubsId,
                                           @Param("inicioDesejado") OffsetDateTime inicioDesejado,
                                           @Param("fimDesejado") OffsetDateTime fimDesejado);

    @Query("SELECT s FROM SlotExame s WHERE s.ubs.latitude BETWEEN :latMin AND :latMax " +
            "AND s.ubs.longitude BETWEEN :lonMin AND :lonMax " +
            "AND s.tipoExame = :tipo AND s.ativo = true " +
            "AND s.dataInicio >= :dataMinima AND s.capacidadeDisponivel > 0")
    List<SlotExame> findSlotsNaRegiao(@Param("tipo") TipoExame tipo,
                                      @Param("dataMinima") OffsetDateTime dataMinima,
                                      @Param("latMin") Double latMin,
                                      @Param("latMax") Double latMax,
                                      @Param("lonMin") Double lonMin,
                                      @Param("lonMax") Double lonMax);
}