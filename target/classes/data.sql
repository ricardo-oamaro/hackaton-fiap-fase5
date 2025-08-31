INSERT INTO paciente (id, nome, ativo, latitude, longitude) VALUES (1, 'Maria Silva', true, -23.5505, -46.6333);
INSERT INTO paciente (id, nome, ativo, latitude, longitude) VALUES (2, 'John Doe', true, -23.5102, -46.6982);
INSERT INTO paciente (id, nome, ativo, latitude, longitude) VALUES (3, 'Maria Santos', true, -22.5505, -47.6333);

-- UBS
INSERT INTO ubs (id, nome, latitude, longitude) VALUES (1, 'UBS Centro', -23.5520, -46.6300);
INSERT INTO ubs (id, nome, latitude, longitude) VALUES (2, 'UBS Norte', -23.5200, -46.6500);

-- UBS especialidades (ElementCollection)
INSERT INTO ubs_especialidades (ubs_id, especialidades) VALUES (1, 'CLINICO_GERAL');
INSERT INTO ubs_especialidades (ubs_id, especialidades) VALUES (2, 'CLINICO_GERAL');

-- Profissionais
INSERT INTO profissional (id, nome, especialidade, ubs_id) VALUES (1, 'Dr. João', 'CLINICO_GERAL', 1);
INSERT INTO profissional (id, nome, especialidade, ubs_id) VALUES (2, 'Dra. Ana', 'CLINICO_GERAL', 2);


INSERT INTO slot_agenda (profissional_id, ubs_id, inicio, fim) VALUES
  (1, 1, TIMESTAMP '2025-09-22 09:00:00', TIMESTAMP '2025-09-22 09:20:00'),
  (2, 2, TIMESTAMP '2025-09-19 14:40:00', TIMESTAMP '2025-09-19 15:00:00'),
  (1, 1, TIMESTAMP '2025-09-23 10:20:00', TIMESTAMP '2025-09-23 10:40:00');