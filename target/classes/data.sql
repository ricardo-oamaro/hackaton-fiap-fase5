INSERT INTO paciente (id, nome, ativo, latitude, longitude) VALUES (1, 'Maria Silva', true, -23.5505, -46.6333);

-- UBS
INSERT INTO ubs (id, nome, latitude, longitude) VALUES (1, 'UBS Centro', -23.5520, -46.6300);
INSERT INTO ubs (id, nome, latitude, longitude) VALUES (2, 'UBS Norte', -23.5200, -46.6500);

-- UBS especialidades (ElementCollection)
INSERT INTO ubs_especialidades (ubs_id, especialidades) VALUES (1, 'CLINICO_GERAL');
INSERT INTO ubs_especialidades (ubs_id, especialidades) VALUES (2, 'CLINICO_GERAL');

-- Profissionais
INSERT INTO profissional (id, nome, especialidade, ubs_id) VALUES (1, 'Dr. João', 'CLINICO_GERAL', 1);
INSERT INTO profissional (id, nome, especialidade, ubs_id) VALUES (2, 'Dra. Ana', 'CLINICO_GERAL', 2);

-- Slots (próximas horas)
INSERT INTO slot_agenda (id, profissional_id, ubs_id, inicio, fim) VALUES (1, 1, 1, DATEADD('MINUTE', 60, CURRENT_TIMESTAMP()), DATEADD('MINUTE', 80, CURRENT_TIMESTAMP()));
INSERT INTO slot_agenda (id, profissional_id, ubs_id, inicio, fim) VALUES (2, 2, 2, DATEADD('MINUTE', 120, CURRENT_TIMESTAMP()), DATEADD('MINUTE', 140, CURRENT_TIMESTAMP()));