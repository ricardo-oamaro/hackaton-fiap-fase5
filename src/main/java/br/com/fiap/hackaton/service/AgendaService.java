package br.com.fiap.hackaton.service;

import br.com.fiap.hackaton.dto.AgendaResponseDto;
import br.com.fiap.hackaton.enums.Especialidade;
import br.com.fiap.hackaton.model.SlotAgenda;
import br.com.fiap.hackaton.repository.ConsultaRepository;
import br.com.fiap.hackaton.repository.SlotAgendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgendaService {

    private final SlotAgendaRepository slotAgendaRepository;
    private final ConsultaRepository consultaRepository;

    public List<AgendaResponseDto> getAgendaByEspecialidade(String especialidade) {
        Especialidade esp = Especialidade.valueOf(especialidade.toUpperCase());
        List<SlotAgenda> slots = slotAgendaRepository.findDisponiveisByEspecialidade(esp, OffsetDateTime.now());

        return slots.stream()
                .map(slot -> new AgendaResponseDto(slot.getProfissional().getNome(), slot.getInicio(), slot.getFim(), slot.getUbs().getNome()))
                .collect(Collectors.toList());
    }

    public void bloquearSlot(Long slotId) {
        SlotAgenda slot = slotAgendaRepository.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Slot não encontrado"));
        slot.setBloqueado(true);
        slotAgendaRepository.save(slot);
    }

    public long contarPacientesAguardando(String especialidade) {
        Especialidade esp = Especialidade.valueOf(especialidade.toUpperCase());
        return consultaRepository.countPacientesAguardando(esp);
    }

}
