# Documentação da API — História 2 (Agendamento de Exames com Triagem)

> **Objetivo**: explicar endpoints, payloads, regras de negócio e boas práticas para evitar erros como `NO_SUGGESTION`, com exemplos fiéis ao que a API espera.

---

## 1) Visão Geral do Domínio
- **Tipo de Exame**: configura metadados usados na **triagem** e na oferta de horários:
  - `urgentePorPadrao` (boolean);
  - `palavrasChaveAlta` / `palavrasChaveMedia` (lista separada por vírgula);
  - prazos por prioridade: `prazoDiasAlta`, `prazoDiasMedia`, `prazoDiasBaixa`.
- **Triagem**:
  - **Alta** quando `urgente = true` (exige `justificativaUrgencia`) ou quando for inerentemente urgente (p. ex., `urgentePorPadrao = true`) ou pelas palavras‑chave de **Alta**.
  - **Média** quando casa com palavras‑chave de **Média** e não é urgente.
  - **Baixa** nos demais casos.
- **Janela & Distância**:
  - A busca usa a janela (por prioridade) e o raio em km para sugerir horários e UBS dentro do alcance, ordenando por **menor prazo** e **proximidade**.
- **Estados do Exame**: `PROPOSTA` → `AGENDADA` (após confirmar) → pode virar `RECUSADA`, `EXPIRADA` (ex.: proposta passou do prazo) ou `CANCELADA` (fluxos administrativos).

---

## 2) Endpoints (collection)

### 2.1 Criar Tipo de Exame
**POST** `/tipos-exame` — Cadastra um **Tipo de Exame**.

**Request (exemplo da sua coleção – ajustar para seu caso):**
```json
{
  "codigo": "CARDIO3",
  "descricao": "cardio Completo",
  "urgentePorPadrao": false,
  "duracaoMinutos": 30,
  "observacoes": "Exame de sangue básico",
  "palavrasChaveAlta": "pré-operatório,cirurgia,urgente,emergência",
  "palavrasChaveMedia": "acompanhamento,controle,rotina médica",
  "prazoDiasAlta": 30,
  "prazoDiasMedia": 7,
  "prazoDiasBaixa": 15
}
```

**Validações (conforme DTO):**
- `codigo`: **obrigatório**, `^[A-Z0-9_]+$`, máx **50** chars.
- `descricao`: **obrigatória**, máx **100** chars.
- `duracaoMinutos`: **> 0**.
- `observacoes`: máx **500** chars.
- `prazoDiasAlta|Media|Baixa`: **≥ 1** (padrões usuais: 3, 7, 15).
- `urgentePorPadrao`: default `false`.

> **Importante**: **o valor de `codigo` precisa bater com `tipoExame` nos agendamentos**.  
> Ex.: se você criar `RAIO_TORAX`, então **agende com** `"tipoExame": "RAIO_TORAX"`.  
> No seu rascunho há `CARDIO3` e `RAIO_TORAX`, mas os agendamentos usam `HEMOGRAMA` e `RAIO_X`. Ajuste para evitar `NOT_FOUND/NO_SUGGESTION`.

---

### 2.2 Listar Tipos de Exame Ativos
**GET** `/tipos-exame` — Lista os tipos de exame disponíveis.

**Uso**: conferir se o `codigo` que você pretende usar em `tipoExame` **existe** e está **ativo**.

---

### 2.3 Listar Exames Disponíveis (pré‑checagem)
**POST** `/exames/disponiveis` — Retorna **opções** de horários (slots) considerando triagem, raio e janela.

**Request (exemplo)**:
```json
{
  "pacienteId": 1,
  "tipoExame": "HEMOGRAMA",
  "latitude": -23.5505,
  "longitude": -46.6333,
  "raioKm": 15,
  "quantidadeMaxima": 5,
  "janelaDias": 15
}
```

**Response (exemplo)**:
```json
{
  "opcoes": [
    {
      "slotId": 1,
      "ubsId": 1,
      "ubsNome": "UBS Centro",
      "tipoExame": "Hemograma Completo",
      "dataInicio": "15/09/2025 08:00",
      "dataFim": "15/09/2025 12:00",
      "vagasDisponiveis": 10,
      "distanciaKm": 0.3
    }
  ],
  "totalEncontrados": 1
}
```

**Validações & defaults**:
- `pacienteId`: obrigatório.
- `tipoExame`: obrigatório.
- `latitude`, `longitude`: obrigatórios (faixas válidas ‑90..90 / ‑180..180).
- `raioKm`: **opcional**, **> 0**, **≤ 100** (se não informado, o serviço usa `app.exame.defaultRadiusKm`, **padrão 20km**).
- `quantidadeMaxima`: default **5** (mín 1).
- `janelaDias`: default **15** (mín 1).

> **Dica**: use este endpoint **antes** de `agendar` para validar que existem horários.  
> Se vier vazio, cadastre/abra **slots** (agenda por UBS/tipo) ou ajuste `raioKm`/`janelaDias`.

---

### 2.4 Agendar Exame (cria **PROPOSTA**)
**POST** `/exames/agendar`

**Exemplo Urgente (Prioridade Alta):**
```json
{
  "pacienteId": 1,
  "tipoExame": "HEMOGRAMA",
  "observacoes": "Exame pré-operatório para cirurgia cardíaca",
  "latitude": -23.5505,
  "longitude": -46.6333,
  "raioKm": 100,
  "urgente": true,
  "justificativaUrgencia": "Cirurgia marcada para próxima semana"
}
```

**Exemplo Normal (Baixa):**
```json
{
  "pacienteId": 2,
  "tipoExame": "HEMOGRAMA",
  "observacoes": "Exame de rotina para check-up anual",
  "latitude": -23.5505,
  "longitude": -46.6333,
  "raioKm": 10,
  "urgente": false
}
```

**Exemplo Média por Palavra‑Chave:**
```json
{
  "pacienteId": 3,
  "tipoExame": "HEMOGRAMA",
  "observacoes": "Exame de acompanhamento para controle de anemia",
  "latitude": -23.5505,
  "longitude": -46.6333,
  "raioKm": 20,
  "urgente": false
}
```

**Exemplo Raio‑X (Alta):**
```json
{
  "pacienteId": 3,
  "tipoExame": "RAIO_X",
  "observacoes": "Suspeita de fratura após queda",
  "latitude": -23.5505,
  "longitude": -46.6333,
  "raioKm": 25,
  "urgente": true,
  "justificativaUrgencia": "Paciente com dor intensa e incapacitante"
}
```

**Regras**:
- `pacienteId` deve **existir** e estar **ativo**.
- `tipoExame` deve **existir** (mesmo **código** criado em `/tipos-exame`) e estar **ativo**.
- Se `urgente = true`, **`justificativaUrgencia` é obrigatória** (até **200** chars).
- `observacoes`: **10..500** chars.
- `latitude`/`longitude`: válidas; `raioKm` opcional (ver defaults do serviço).

**Response (exemplo)**:
```json
{
  "exameId": 5,
  "ubsId": 1,
  "ubsNome": "UBS Centro",
  "tipoExame": "Hemograma Completo",
  "status": "PROPOSTA",
  "dataAgendada": "15/09/2025 08:00",
  "dataLimite": "18/09/2025 23:59",
  "distanciaKm": 0.3,
  "prioridadeTriagem": "Alta",
  "justificativaTriagem": "Exame marcado como urgente",
  "expiresAt": "12/09/2025 17:45"
}
```

> **Sobre datas**: os DTOs usam `@JsonFormat` com timezone **America/Sao_Paulo** (formato `dd/MM/yyyy HH:mm`).  
> Se você preferir ISO‑8601, alinhe a configuração Jackson.

---

### 2.5 Confirmar Exame (muda para **AGENDADA**)
**POST** `/exames/{{exameId}}/confirmar`

**Boas práticas no Postman**:
- No request de **Agendar**, salve o id:
  ```js
  const j = pm.response.json();
  pm.collectionVariables.set("exameId", j.exameId || j.id);
  ```
- Use `{{exameId}}` na URL de **confirmar**.  
  > Evite URLs fixas como `/exames/1/confirmar` (quebram quando o id muda).

---

## 3) Códigos de Erro (padrão `ErrorResponseDto`)
| Código             | HTTP | Descrição                                     | Causa típica                                 |
|--------------------|------|-----------------------------------------------|----------------------------------------------|
| `NO_SUGGESTION`    | 422  | Nenhum horário disponível                     | Sem slots dentro do raio/janela/capacidade   |
| `VALIDATION_ERROR` | 400  | Erro de validação de payload                  | Campo obrigatório faltando / formato inválido|
| `NOT_FOUND`        | 404  | Recurso não encontrado                        | `tipoExame` inexistente / id inválido        |
| `CONFLICT`         | 409  | Conflito de estado                            | Ex.: confirmar fora do estado permitido      |
| `BAD_REQUEST`      | 400  | Requisição inválida                           | Parâmetro incorreto/regra de negócio violada |

**Shape do erro:**
```json
{
  "code": "NO_SUGGESTION",
  "message": "Nenhum horário disponível encontrado dentro do prazo e raio especificados",
  "timestamp": "2025-09-12T17:28:14.1063498-03:00"
}
```

---

## 4) Propriedades de Configuração Relevantes
- `app.exame.defaultRadiusKm`: **20** (km) — raio padrão quando `raioKm` não informado no request.
- `app.exame.holdMinutes`: **10** (min) — tempo de **reserva** da proposta (`expiresAt`).
- `app.exame.cleanupIntervalMinutes`: **30** (min) — intervalo de limpeza de propostas expiradas (**observação abaixo**).

> **Observação**: o `@Scheduled(fixedDelayString = "${{app.exame.cleanupIntervalMinutes:30}}000")` multiplica o valor **apenas por 1000** (segundos → ms).  
> Se o property estiver em **minutos**, o job rodará a cada ~30 **segundos**, não 30 **minutos**.  
> **Sugestões**:
> - Usar SpEL: `fixedDelayString = "#{{${{app.exame.cleanupIntervalMinutes:30}} * 60 * 1000}}"`
> - **ou** alterar a propriedade para estar em **milissegundos** e remover a multiplicação.

---

## 5) Fluxos Recomendados
### 5.1 Urgente (Alta)
1. (Opcional) **Pré‑checagem** `/exames/disponiveis` com `raioKm` mais amplo.
2. **Agendar** com `urgente = true` e `justificativaUrgencia`.
3. **Confirmar** antes do `expiresAt`.

### 5.2 Normal/Média
1. **Criar/validar** o `Tipo de Exame` (palavras‑chave de **Média**).
2. **Pré‑checagem** `/exames/disponiveis` com janela adequada (ex.: 7–15 dias).
3. **Agendar** e **Confirmar**.

> **Dica operacional**: mantenha uma rotina de **seed de slots** (agenda por UBS/tipo) para evitar `NO_SUGGESTION` durante testes.

---

## 6) Dicas de compatibilidade com a sua Collection
- **Sincronize `codigo` × `tipoExame`**:
  - Se criar `CARDIO3`, **agende com** `"tipoExame": "CARDIO3"`.
  - Se criar `RAIO_TORAX`, **agende com** `"tipoExame": "RAIO_TORAX"`.
- **Capture `exameId`** e use `{{exameId}}` em `/exames/{{exameId}}/confirmar`.
- Considere adicionar requests para **`/exames/{id}/recusar`** e **`/exames/{id}/reagendar`** (existem no backend), mantendo o fluxo completo de usuário.

---

## 7) Exemplos de cURL

**Agendar — Urgente**
```bash
curl --location 'http://localhost:8080/exames/agendar' \
  --header 'Content-Type: application/json' \
  --data '{{"pacienteId":1,"tipoExame":"HEMOGRAMA","observacoes":"Pré-operatório","latitude":-23.5505,"longitude":-46.6333,"raioKm":100,"urgente":true,"justificativaUrgencia":"Cirurgia na próxima semana"}}'
```

**Listar Disponíveis**
```bash
curl --location 'http://localhost:8080/exames/disponiveis' \
  --header 'Content-Type: application/json' \
  --data '{{"pacienteId":1,"tipoExame":"HEMOGRAMA","latitude":-23.5505,"longitude":-46.6333,"raioKm":15,"quantidadeMaxima":5,"janelaDias":15}}'
```

**Confirmar**
```bash
curl --location 'http://localhost:8080/exames/{{exameId}}/confirmar' -X POST
```

---

## 8) Checklist de Qualidade
- [ ] Tipos de Exame criados com `codigo` consistente.
- [ ] Slots de agenda existem no raio/janela e com **capacidadeDisponivel > 0**.
- [ ] Para urgência, **`justificativaUrgencia`** preenchida.
- [ ] `raioKm` omitido? Serviço usará **20km** por padrão.
- [ ] Capturou `exameId` e confirmou antes do `expiresAt`.
- [ ] (Opcional) Ajustou o `@Scheduled` do cleanup para refletir **minutos**.

---

**FIM** — Esta documentação refina seu rascunho, alinhando exemplos e regras às validações dos DTOs e à configuração do serviço.
