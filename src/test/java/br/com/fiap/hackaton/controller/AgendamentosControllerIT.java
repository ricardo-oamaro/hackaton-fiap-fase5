package br.com.fiap.hackaton.controller;

import br.com.fiap.hackaton.model.Consulta;
import br.com.fiap.hackaton.repository.ConsultaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AgendamentosControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Test
    void sugerir_e_confirmar_fluxo_sucesso() throws Exception {
        var sugestaoJson = "{\n" +
                "  \"pacienteId\": 1,\n" +
                "  \"tipoConsulta\": \"clinico_geral\",\n" +
                "  \"sintomas\": \"Febre e dor de cabeça\",\n" +
                "  \"latitude\": -23.5505,\n" +
                "  \"longitude\": -46.6333,\n" +
                "  \"raioKm\": 15\n" +
                "}";

        var mvcResult = mockMvc.perform(post("/agendamentos/sugestao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sugestaoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.consultaId").exists())
                .andExpect(jsonPath("$.profissionalNome").isString())
                .andExpect(jsonPath("$.ubsNome").isString())
                .andReturn();

        var resp = mvcResult.getResponse().getContentAsString();
        var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        var node = mapper.readTree(resp);
        long consultaId = node.get("consultaId").asLong();

        mockMvc.perform(post("/agendamentos/" + consultaId + "/confirmar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.consultaId").value((int) consultaId))
                .andExpect(jsonPath("$.status").value("AGENDADA"));

        Consulta c = consultaRepository.findById(consultaId).orElseThrow();
        assertThat(c.getStatus().name()).isEqualTo("AGENDADA");
    }

    @Test
    void sugerir_erro_validacao_ausencia_paciente() throws Exception {
        var invalidJson = "{\n  \"tipoConsulta\": \"clinico_geral\",\n  \"sintomas\": \"Tosse\",\n  \"latitude\": -23.55,\n  \"longitude\": -46.63\n}";

        mockMvc.perform(post("/agendamentos/sugestao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.body").exists());
    }

    @Test
    void listar_sugestoes_e_erro_validacao() throws Exception {
        var req = "{\n  \"pacienteId\": 1,\n  \"tipoConsulta\": \"clinico_geral\",\n  \"sintomas\": \"Dor\",\n  \"latitude\": -23.55,\n  \"longitude\": -46.63\n}";

        mockMvc.perform(post("/agendamentos/sugestoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.opcoes").isArray());

        var badReq = "{\n  \"pacienteId\": 1,\n  \"tipoConsulta\": \"clinico_geral\",\n  \"sintomas\": \"\",\n  \"latitude\": -23.55,\n  \"longitude\": -46.63\n}";

        mockMvc.perform(post("/agendamentos/sugestoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badReq))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.body").exists());
    }

    @Test
    void confirmar_nao_encontrado_retorna_404() throws Exception {
        mockMvc.perform(post("/agendamentos/999999/confirmar").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
