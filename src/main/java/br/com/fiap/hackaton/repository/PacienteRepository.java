package br.com.fiap.hackaton.repository;

import br.com.fiap.hackaton.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}
