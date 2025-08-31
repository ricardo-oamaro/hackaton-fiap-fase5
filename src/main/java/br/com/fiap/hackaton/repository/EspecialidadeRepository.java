package br.com.fiap.hackaton.repository;

import br.com.fiap.hackaton.model.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {
    List<Especialidade> findByAtivo(boolean ativo);
}