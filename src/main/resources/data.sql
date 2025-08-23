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

-- Slots de agenda
INSERT INTO slot_agenda (profissional_id, ubs_id, inicio, fim) VALUES
   (1, 1, TIMESTAMP '2025-09-22 09:00:00', TIMESTAMP '2025-09-22 09:20:00'),
   (2, 2, TIMESTAMP '2025-09-19 14:40:00', TIMESTAMP '2025-09-19 15:00:00'),
   (1, 1, TIMESTAMP '2025-09-23 10:20:00', TIMESTAMP '2025-09-23 10:40:00');

-- Tipos de Exame
INSERT INTO tipo_exame (id, codigo, descricao, urgente_por_padrao, ativo, duracao_minutos, observacoes, palavras_chave_alta, palavras_chave_media, prazo_dias_alta, prazo_dias_media, prazo_dias_baixa, criado_em, atualizado_em) VALUES
    (1, 'HEMOGRAMA', 'Hemograma Completo', false, true, 15, 'Análise do sangue completa', 'pré-operatório,cirurgia,urgente,emergência', 'acompanhamento,controle,rotina', 3, 7, 15, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'RAIO_X', 'Raio X Tórax', false, true, 20, 'Exame de imagem do tórax', 'trauma,fratura,dor intensa', 'acompanhamento,checkup', 2, 5, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 'ULTRASSOM', 'Ultrassom Abdominal', false, true, 30, 'Exame de ultrassom', 'dor abdominal,emergência', 'rotina,preventivo', 5, 10, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Slots de Exame
INSERT INTO slot_exame (ubs_id, tipo_exame_id, data_inicio, data_fim, capacidade_total, capacidade_disponivel, ativo) VALUES
    (1, 1, TIMESTAMP '2025-08-25 08:00:00', TIMESTAMP '2025-08-25 12:00:00', 10, 10, true),
    (1, 1, TIMESTAMP '2025-08-26 08:00:00', TIMESTAMP '2025-08-26 12:00:00', 8, 8, true),
    (2, 1, TIMESTAMP '2025-08-25 14:00:00', TIMESTAMP '2025-08-25 18:00:00', 6, 6, true),
    (1, 2, TIMESTAMP '2025-08-25 09:00:00', TIMESTAMP '2025-08-25 11:00:00', 5, 5, true),
    (2, 2, TIMESTAMP '2025-08-26 15:00:00', TIMESTAMP '2025-08-26 17:00:00', 4, 4, true),
    (1, 3, TIMESTAMP '2025-08-27 10:00:00', TIMESTAMP '2025-08-27 16:00:00', 3, 3, true),
    (2, 3, TIMESTAMP '2025-08-28 08:00:00', TIMESTAMP '2025-08-28 14:00:00', 5, 5, true);
