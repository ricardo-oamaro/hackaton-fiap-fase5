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
--INSERT INTO slot_agenda (id, profissional_id, ubs_id, inicio, fim) VALUES (1, 1, 1, DATEADD('MINUTE', 60, CURRENT_TIMESTAMP()), DATEADD('MINUTE', 80, CURRENT_TIMESTAMP()));
--INSERT INTO slot_agenda (id, profissional_id, ubs_id, inicio, fim) VALUES (2, 2, 2, DATEADD('MINUTE', 120, CURRENT_TIMESTAMP()), DATEADD('MINUTE', 140, CURRENT_TIMESTAMP()));

--INSERT INTO slot_agenda (profissional_id, ubs_id, inicio, fim) VALUES
--  (1, 1, TIMESTAMP '2025-08-22 09:00:00', TIMESTAMP '2025-08-22 09:20:00'),
--  (2, 2, TIMESTAMP '2025-08-19 14:40:00', TIMESTAMP '2025-08-19 15:00:00'),
--  (1, 1, TIMESTAMP '2025-08-23 10:20:00', TIMESTAMP '2025-08-23 10:40:00');

INSERT INTO slot_agenda (profissional_id, ubs_id, inicio, fim)
VALUES (1, 1,
        DATEADD('DAY', 1, DATEADD('HOUR', 9, CURRENT_DATE())),
        DATEADD('DAY', 1, DATEADD('HOUR', 9, DATEADD('MINUTE', 20, CURRENT_DATE()))));

-- Depois de amanhã às 14:00
INSERT INTO slot_agenda (profissional_id, ubs_id, inicio, fim)
VALUES (2, 2,
        DATEADD('DAY', 2, DATEADD('HOUR', 14, CURRENT_DATE())),
        DATEADD('DAY', 2, DATEADD('HOUR', 14, DATEADD('MINUTE', 20, CURRENT_DATE()))));

-- Daqui a 3 dias às 10:30
INSERT INTO slot_agenda (profissional_id, ubs_id, inicio, fim)
VALUES (1, 1,
        DATEADD('DAY', 3, DATEADD('HOUR', 10, DATEADD('MINUTE', 30, CURRENT_DATE()))),
        DATEADD('DAY', 3, DATEADD('HOUR', 10, DATEADD('MINUTE', 50, CURRENT_DATE()))));