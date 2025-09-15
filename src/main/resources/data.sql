-- Pacientes
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

-- Slots de agenda (consultas) - Datas ao longo de 6 meses (Setembro 2025 a Março 2026)
-- Setembro 2025
INSERT INTO slot_agenda (id, profissional_id, ubs_id, inicio, fim) VALUES
                                                                       (1, 1, 1, TIMESTAMP '2025-09-15 09:00:00', TIMESTAMP '2025-09-15 09:30:00'),
                                                                       (2, 1, 1, TIMESTAMP '2025-09-15 10:00:00', TIMESTAMP '2025-09-15 10:30:00'),
                                                                       (3, 2, 2, TIMESTAMP '2025-09-15 14:00:00', TIMESTAMP '2025-09-15 14:30:00'),
                                                                       (4, 2, 2, TIMESTAMP '2025-09-15 15:00:00', TIMESTAMP '2025-09-15 15:30:00'),

-- Outubro 2025
                                                                       (5, 1, 1, TIMESTAMP '2025-10-01 09:00:00', TIMESTAMP '2025-10-01 09:30:00'),
                                                                       (6, 1, 1, TIMESTAMP '2025-10-01 10:00:00', TIMESTAMP '2025-10-01 10:30:00'),
                                                                       (7, 2, 2, TIMESTAMP '2025-10-01 14:00:00', TIMESTAMP '2025-10-01 14:30:00'),
                                                                       (8, 2, 2, TIMESTAMP '2025-10-01 15:00:00', TIMESTAMP '2025-10-01 15:30:00'),

-- Novembro 2025
                                                                       (9, 1, 1, TIMESTAMP '2025-11-10 09:00:00', TIMESTAMP '2025-11-10 09:30:00'),
                                                                       (10, 1, 1, TIMESTAMP '2025-11-10 10:00:00', TIMESTAMP '2025-11-10 10:30:00'),
                                                                       (11, 2, 2, TIMESTAMP '2025-11-10 14:00:00', TIMESTAMP '2025-11-10 14:30:00'),
                                                                       (12, 2, 2, TIMESTAMP '2025-11-10 15:00:00', TIMESTAMP '2025-11-10 15:30:00'),

-- Dezembro 2025
                                                                       (13, 1, 1, TIMESTAMP '2025-12-05 09:00:00', TIMESTAMP '2025-12-05 09:30:00'),
                                                                       (14, 1, 1, TIMESTAMP '2025-12-05 10:00:00', TIMESTAMP '2025-12-05 10:30:00'),
                                                                       (15, 2, 2, TIMESTAMP '2025-12-05 14:00:00', TIMESTAMP '2025-12-05 14:30:00'),
                                                                       (16, 2, 2, TIMESTAMP '2025-12-05 15:00:00', TIMESTAMP '2025-12-05 15:30:00'),

-- Janeiro 2026
                                                                       (17, 1, 1, TIMESTAMP '2026-01-15 09:00:00', TIMESTAMP '2026-01-15 09:30:00'),
                                                                       (18, 1, 1, TIMESTAMP '2026-01-15 10:00:00', TIMESTAMP '2026-01-15 10:30:00'),
                                                                       (19, 2, 2, TIMESTAMP '2026-01-15 14:00:00', TIMESTAMP '2026-01-15 14:30:00'),
                                                                       (20, 2, 2, TIMESTAMP '2026-01-15 15:00:00', TIMESTAMP '2026-01-15 15:30:00'),

-- Fevereiro 2026
                                                                       (21, 1, 1, TIMESTAMP '2026-02-10 09:00:00', TIMESTAMP '2026-02-10 09:30:00'),
                                                                       (22, 1, 1, TIMESTAMP '2026-02-10 10:00:00', TIMESTAMP '2026-02-10 10:30:00'),
                                                                       (23, 2, 2, TIMESTAMP '2026-02-10 14:00:00', TIMESTAMP '2026-02-10 14:30:00'),
                                                                       (24, 2, 2, TIMESTAMP '2026-02-10 15:00:00', TIMESTAMP '2026-02-10 15:30:00'),

-- Março 2026
                                                                       (25, 1, 1, TIMESTAMP '2026-03-20 09:00:00', TIMESTAMP '2026-03-20 09:30:00'),
                                                                       (26, 1, 1, TIMESTAMP '2026-03-20 10:00:00', TIMESTAMP '2026-03-20 10:30:00'),
                                                                       (27, 2, 2, TIMESTAMP '2026-03-20 14:00:00', TIMESTAMP '2026-03-20 14:30:00'),
                                                                       (28, 2, 2, TIMESTAMP '2026-03-20 15:00:00', TIMESTAMP '2026-03-20 15:30:00');

-- Tipos de Exame
INSERT INTO tipo_exame (id, codigo, descricao, urgente_por_padrao, ativo, duracao_minutos, observacoes, palavras_chave_alta, palavras_chave_media, prazo_dias_alta, prazo_dias_media, prazo_dias_baixa, criado_em, atualizado_em) VALUES
                                                                                                                                                                                                                                      (1, 'HEMOGRAMA', 'Hemograma Completo', false, true, 15, 'Análise do sangue completa', 'pré-operatório,cirurgia,urgente,emergência', 'acompanhamento,controle,rotina', 3, 7, 15, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                                                                                                                      (2, 'RAIO_X', 'Raio X Tórax', false, true, 20, 'Exame de imagem do tórax', 'trauma,fratura,dor intensa', 'acompanhamento,checkup', 2, 5, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                                                                                                                      (3, 'ULTRASSOM', 'Ultrassom Abdominal', false, true, 30, 'Exame de ultrassom', 'dor abdominal,emergência', 'rotina,preventivo', 5, 10, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Slots de Exame - Datas ao longo de 6 meses (Setembro 2025 a Março 2026)
-- Setembro 2025
INSERT INTO slot_exame (id, ubs_id, tipo_exame_id, data_inicio, data_fim, capacidade_total, capacidade_disponivel, ativo) VALUES
                                                                                                                              (1, 1, 1, TIMESTAMP '2025-09-15 08:00:00', TIMESTAMP '2025-09-15 12:00:00', 10, 10, true),
                                                                                                                              (2, 1, 1, TIMESTAMP '2025-09-16 08:00:00', TIMESTAMP '2025-09-16 12:00:00', 10, 10, true),
                                                                                                                              (3, 2, 1, TIMESTAMP '2025-09-15 14:00:00', TIMESTAMP '2025-09-15 18:00:00', 10, 10, true),
                                                                                                                              (4, 2, 1, TIMESTAMP '2025-09-16 14:00:00', TIMESTAMP '2025-09-16 18:00:00', 10, 10, true),

-- Outubro 2025
                                                                                                                              (5, 1, 1, TIMESTAMP '2025-10-01 08:00:00', TIMESTAMP '2025-10-01 12:00:00', 10, 10, true),
                                                                                                                              (6, 2, 1, TIMESTAMP '2025-10-01 14:00:00', TIMESTAMP '2025-10-01 18:00:00', 10, 10, true),

-- Novembro 2025
                                                                                                                              (7, 1, 1, TIMESTAMP '2025-11-10 08:00:00', TIMESTAMP '2025-11-10 12:00:00', 10, 10, true),
                                                                                                                              (8, 2, 1, TIMESTAMP '2025-11-10 14:00:00', TIMESTAMP '2025-11-10 18:00:00', 10, 10, true),

-- Dezembro 2025
                                                                                                                              (9, 1, 1, TIMESTAMP '2025-12-05 08:00:00', TIMESTAMP '2025-12-05 12:00:00', 10, 10, true),
                                                                                                                              (10, 2, 1, TIMESTAMP '2025-12-05 14:00:00', TIMESTAMP '2025-12-05 18:00:00', 10, 10, true),

-- Janeiro 2026
                                                                                                                              (11, 1, 1, TIMESTAMP '2026-01-15 08:00:00', TIMESTAMP '2026-01-15 12:00:00', 10, 10, true),
                                                                                                                              (12, 2, 1, TIMESTAMP '2026-01-15 14:00:00', TIMESTAMP '2026-01-15 18:00:00', 10, 10, true),

-- Fevereiro 2026
                                                                                                                              (13, 1, 1, TIMESTAMP '2026-02-10 08:00:00', TIMESTAMP '2026-02-10 12:00:00', 10, 10, true),
                                                                                                                              (14, 2, 1, TIMESTAMP '2026-02-10 14:00:00', TIMESTAMP '2026-02-10 18:00:00', 10, 10, true),

-- Março 2026
                                                                                                                              (15, 1, 1, TIMESTAMP '2026-03-20 08:00:00', TIMESTAMP '2026-03-20 12:00:00', 10, 10, true),
                                                                                                                              (16, 2, 1, TIMESTAMP '2026-03-20 14:00:00', TIMESTAMP '2026-03-20 18:00:00', 10, 10, true);

-- Adicionando slots para os outros tipos de exame (RAIO_X e ULTRASSOM) de forma similar
-- Setembro 2025
INSERT INTO slot_exame (id, ubs_id, tipo_exame_id, data_inicio, data_fim, capacidade_total, capacidade_disponivel, ativo) VALUES
                                                                                                                              (17, 1, 2, TIMESTAMP '2025-09-15 09:00:00', TIMESTAMP '2025-09-15 11:00:00', 5, 5, true),
                                                                                                                              (18, 2, 2, TIMESTAMP '2025-09-16 15:00:00', TIMESTAMP '2025-09-16 17:00:00', 5, 5, true),
                                                                                                                              (19, 1, 3, TIMESTAMP '2025-09-17 10:00:00', TIMESTAMP '2025-09-17 16:00:00', 3, 3, true),
                                                                                                                              (20, 2, 3, TIMESTAMP '2025-09-18 08:00:00', TIMESTAMP '2025-09-18 14:00:00', 5, 5, true);

-- Outubro 2025
INSERT INTO slot_exame (id, ubs_id, tipo_exame_id, data_inicio, data_fim, capacidade_total, capacidade_disponivel, ativo) VALUES
                                                                                                                              (21, 1, 2, TIMESTAMP '2025-10-01 09:00:00', TIMESTAMP '2025-10-01 11:00:00', 5, 5, true),
                                                                                                                              (22, 2, 2, TIMESTAMP '2025-10-02 15:00:00', TIMESTAMP '2025-10-02 17:00:00', 5, 5, true),
                                                                                                                              (23, 1, 3, TIMESTAMP '2025-10-03 10:00:00', TIMESTAMP '2025-10-03 16:00:00', 3, 3, true),
                                                                                                                              (24, 2, 3, TIMESTAMP '2025-10-04 08:00:00', TIMESTAMP '2025-10-04 14:00:00', 5, 5, true);

-- Novembro 2025
INSERT INTO slot_exame (id, ubs_id, tipo_exame_id, data_inicio, data_fim, capacidade_total, capacidade_disponivel, ativo) VALUES
                                                                                                                              (25, 1, 2, TIMESTAMP '2025-11-10 09:00:00', TIMESTAMP '2025-11-10 11:00:00', 5, 5, true),
                                                                                                                              (26, 2, 2, TIMESTAMP '2025-11-11 15:00:00', TIMESTAMP '2025-11-11 17:00:00', 5, 5, true),
                                                                                                                              (27, 1, 3, TIMESTAMP '2025-11-12 10:00:00', TIMESTAMP '2025-11-12 16:00:00', 3, 3, true),
                                                                                                                              (28, 2, 3, TIMESTAMP '2025-11-13 08:00:00', TIMESTAMP '2025-11-13 14:00:00', 5, 5, true);

-- Dezembro 2025
INSERT INTO slot_exame (id, ubs_id, tipo_exame_id, data_inicio, data_fim, capacidade_total, capacidade_disponivel, ativo) VALUES
                                                                                                                              (29, 1, 2, TIMESTAMP '2025-12-05 09:00:00', TIMESTAMP '2025-12-05 11:00:00', 5, 5, true),
                                                                                                                              (30, 2, 2, TIMESTAMP '2025-12-06 15:00:00', TIMESTAMP '2025-12-06 17:00:00', 5, 5, true),
                                                                                                                              (31, 1, 3, TIMESTAMP '2025-12-07 10:00:00', TIMESTAMP '2025-12-07 16:00:00', 3, 3, true),
                                                                                                                              (32, 2, 3, TIMESTAMP '2025-12-08 08:00:00', TIMESTAMP '2025-12-08 14:00:00', 5, 5, true);

-- Janeiro 2026
INSERT INTO slot_exame (id, ubs_id, tipo_exame_id, data_inicio, data_fim, capacidade_total, capacidade_disponivel, ativo) VALUES
                                                                                                                              (33, 1, 2, TIMESTAMP '2026-01-15 09:00:00', TIMESTAMP '2026-01-15 11:00:00', 5, 5, true),
                                                                                                                              (34, 2, 2, TIMESTAMP '2026-01-16 15:00:00', TIMESTAMP '2026-01-16 17:00:00', 5, 5, true),
                                                                                                                              (35, 1, 3, TIMESTAMP '2026-01-17 10:00:00', TIMESTAMP '2026-01-17 16:00:00', 3, 3, true),
                                                                                                                              (36, 2, 3, TIMESTAMP '2026-01-18 08:00:00', TIMESTAMP '2026-01-18 14:00:00', 5, 5, true);

-- Fevereiro 2026
INSERT INTO slot_exame (id, ubs_id, tipo_exame_id, data_inicio, data_fim, capacidade_total, capacidade_disponivel, ativo) VALUES
                                                                                                                              (37, 1, 2, TIMESTAMP '2026-02-10 09:00:00', TIMESTAMP '2026-02-10 11:00:00', 5, 5, true),
                                                                                                                              (38, 2, 2, TIMESTAMP '2026-02-11 15:00:00', TIMESTAMP '2026-02-11 17:00:00', 5, 5, true),
                                                                                                                              (39, 1, 3, TIMESTAMP '2026-02-12 10:00:00', TIMESTAMP '2026-02-12 16:00:00', 3, 3, true),
                                                                                                                              (40, 2, 3, TIMESTAMP '2026-02-13 08:00:00', TIMESTAMP '2026-02-13 14:00:00', 5, 5, true);

-- Março 2026
INSERT INTO slot_exame (id, ubs_id, tipo_exame_id, data_inicio, data_fim, capacidade_total, capacidade_disponivel, ativo) VALUES
                                                                                                                              (41, 1, 2, TIMESTAMP '2026-03-20 09:00:00', TIMESTAMP '2026-03-20 11:00:00', 5, 5, true),
                                                                                                                              (42, 2, 2, TIMESTAMP '2026-03-21 15:00:00', TIMESTAMP '2026-03-21 17:00:00', 5, 5, true),
                                                                                                                              (43, 1, 3, TIMESTAMP '2026-03-22 10:00:00', TIMESTAMP '2026-03-22 16:00:00', 3, 3, true),
                                                                                                                              (44, 2, 3, TIMESTAMP '2026-03-23 08:00:00', TIMESTAMP '2026-03-23 14:00:00', 5, 5, true);
INSERT INTO slot_exame (id, ubs_id, tipo_exame_id, data_inicio, data_fim, capacidade_total, capacidade_disponivel, ativo) VALUES
-- Slots para os próximos 2 dias (prioridade alta)
(45, 1, 2, TIMESTAMP '2025-09-13 09:00:00', TIMESTAMP '2025-09-13 11:00:00', 5, 5, true),
(46, 1, 2, TIMESTAMP '2025-09-13 14:00:00', TIMESTAMP '2025-09-13 16:00:00', 5, 5, true),
(47, 2, 2, TIMESTAMP '2025-09-14 09:00:00', TIMESTAMP '2025-09-14 11:00:00', 5, 5, true),
(48, 2, 2, TIMESTAMP '2025-09-14 14:00:00', TIMESTAMP '2025-09-14 16:00:00', 5, 5, true),

-- Slots adicionais para a mesma semana
(49, 1, 2, TIMESTAMP '2025-09-15 09:00:00', TIMESTAMP '2025-09-15 11:00:00', 5, 5, true),
(50, 2, 2, TIMESTAMP '2025-09-15 14:00:00', TIMESTAMP '2025-09-15 16:00:00', 5, 5, true),
(51, 1, 2, TIMESTAMP '2025-09-16 09:00:00', TIMESTAMP '2025-09-16 11:00:00', 5, 5, true),
(52, 2, 2, TIMESTAMP '2025-09-16 14:00:00', TIMESTAMP '2025-09-16 16:00:00', 5, 5, true);