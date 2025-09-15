package br.com.fiap.hackaton.controller;

import br.com.fiap.hackaton.enums.Especialidade;
import br.com.fiap.hackaton.model.SlotAgenda;
import br.com.fiap.hackaton.repository.ConsultaRepository;
import br.com.fiap.hackaton.repository.SlotAgendaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AgendaControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SlotAgendaRepository slotAgendaRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Test
    void shouldReturnAgendaByEspecialidade() throws Exception {
        mockMvc.perform(get("/agenda/clinico_geral").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].data").exists())
                .andExpect(jsonPath("$[0].profissionais").isArray())
                .andExpect(jsonPath("$[0].profissionais[0].profissionalNome").isString())
                .andExpect(jsonPath("$[0].profissionais[0].slots").isArray())
                .andExpect(jsonPath("$[0].profissionais[0].slots[0].status").isString());
    }

    @Test
    void bloquearSlot_authorized_updatesSlot() throws Exception {
        Optional<SlotAgenda> before = slotAgendaRepository.findById(1L);
        assertThat(before).isPresent();
        assertThat(before.get().isBloqueado()).isFalse();

        mockMvc.perform(patch("/agenda/slots/1/bloquear").header("X-User-Role", "ADMIN"))
                .andExpect(status().isNoContent());

        SlotAgenda after = slotAgendaRepository.findById(1L).orElseThrow();
        assertThat(after.isBloqueado()).isTrue();
    }

    @Test
    void bloquearSlot_forbidden_withoutAdmin() throws Exception {
        mockMvc.perform(patch("/agenda/slots/1/bloquear"))
                .andExpect(status().isForbidden());
    }

    @Test
    void desbloquearSlot_authorized_updatesSlot() throws Exception {
        // ensure slot is blocked first
        SlotAgenda slot = slotAgendaRepository.findById(1L).orElseThrow();
        slot.setBloqueado(true);
        slotAgendaRepository.save(slot);

        mockMvc.perform(patch("/agenda/slots/1/desbloquear").header("X-User-Role", "ADMIN"))
                .andExpect(status().isNoContent());

        SlotAgenda after = slotAgendaRepository.findById(1L).orElseThrow();
        assertThat(after.isBloqueado()).isFalse();
    }

    @Test
    void contarAguardando_matchesRepositoryCount() throws Exception {
        long expected = consultaRepository.countPacientesAguardando(Especialidade.CLINICO_GERAL);

        mockMvc.perform(get("/agenda/clinico_geral/aguardando").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value((int) expected));
    }
}
