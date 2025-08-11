package br.com.fiap.hackaton.repository;

import br.com.fiap.hackaton.enums.Especialidade;
import br.com.fiap.hackaton.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

    List<Profissional> findByEspecialidade(Especialidade especialidade);
}
