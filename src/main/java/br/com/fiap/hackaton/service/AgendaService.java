package br.com.fiap.hackaton.service;

import br.com.fiap.hackaton.dto.AgendaDiaDto;
import br.com.fiap.hackaton.dto.ProfissionalAgendaDto;
import br.com.fiap.hackaton.dto.SlotDto;
import br.com.fiap.hackaton.enums.Especialidade;
import br.com.fiap.hackaton.model.SlotAgenda;
import br.com.fiap.hackaton.repository.ConsultaRepository;
import br.com.fiap.hackaton.repository.SlotAgendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgendaService {

    private final SlotAgendaRepository slotAgendaRepository;
    private final ConsultaRepository consultaRepository;

    public List<AgendaDiaDto> getAgendaByEspecialidade(String especialidade) {
        Especialidade esp = Especialidade.valueOf(especialidade.toUpperCase());
        List<SlotAgenda> slots = slotAgendaRepository.findDisponiveisByEspecialidade(esp, OffsetDateTime.now());

        Set<Long> agendados = new HashSet<>(consultaRepository.findSlotsAgendados());
        Set<Long> propostas = new HashSet<>(consultaRepository.findSlotsPropostaAtivas());

        Map<LocalDate, Map<Long, List<SlotAgenda>>> grouped = slots.stream()
                .collect(Collectors.groupingBy(s -> s.getInicio().toLocalDate(),
                        Collectors.groupingBy(s -> s.getProfissional().getId())));

        return grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    LocalDate data = entry.getKey();
                    Map<Long, List<SlotAgenda>> porProf = entry.getValue();

                    List<ProfissionalAgendaDto> profissionais = porProf.values().stream()
                            .map(list -> {
                                String profName = list.get(0).getProfissional().getNome();
                                List<SlotDto> slotDtos = list.stream()
                                        .sorted(Comparator.comparing(SlotAgenda::getInicio))
                                        .map(s -> {
                                            String status;
                                            if (s.isBloqueado()) status = "BLOQUEADO";
                                            else if (agendados.contains(s.getId())) status = "AGENDADA";
                                            else if (propostas.contains(s.getId())) status = "PROPOSTA";
                                            else status = "VAGO";
                                            return new SlotDto(s.getId(), s.getInicio(), s.getFim(), status, s.getUbs().getNome());
                                        })
                                        .collect(Collectors.toList());
                                return new ProfissionalAgendaDto(profName, slotDtos);
                            })
                            .sorted(Comparator.comparing(ProfissionalAgendaDto::profissionalNome))
                            .collect(Collectors.toList());

                    return new AgendaDiaDto(data.toString(), profissionais);
                })
                .collect(Collectors.toList());
    }

    public void bloquearSlot(Long slotId) {
        SlotAgenda slot = slotAgendaRepository.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Slot não encontrado"));
        slot.setBloqueado(true);
        slotAgendaRepository.save(slot);
    }

    public void desbloquearSlot(Long slotId) {
        SlotAgenda slot = slotAgendaRepository.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Slot não encontrado"));
        slot.setBloqueado(false);
        slotAgendaRepository.save(slot);
    }

    public long contarPacientesAguardando(String especialidade) {
        Especialidade esp = Especialidade.valueOf(especialidade.toUpperCase());
        return consultaRepository.countPacientesAguardando(esp);
    }

}
