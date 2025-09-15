package br.com.fiap.hackaton.controller;

import br.com.fiap.hackaton.dto.AgendaDiaDto;
import br.com.fiap.hackaton.service.AgendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agenda")
@RequiredArgsConstructor
public class AgendaController {

    private final AgendaService agendaService;

    @GetMapping("/{especialidade}")
    public ResponseEntity<List<AgendaDiaDto>> getAgendaByEspecialidade(@PathVariable String especialidade) {
        List<AgendaDiaDto> agenda = agendaService.getAgendaByEspecialidade(especialidade);
        return ResponseEntity.ok(agenda);
    }

    @PatchMapping("/slots/{id}/bloquear")
    public ResponseEntity<Void> bloquearSlot(@PathVariable Long id,
                                             @RequestHeader(value = "X-User-Role", required = false) String role) {
        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            agendaService.bloquearSlot(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping("/slots/{id}/desbloquear")
    public ResponseEntity<Void> desbloquearSlot(@PathVariable Long id,
                                                @RequestHeader(value = "X-User-Role", required = false) String role) {
        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            agendaService.desbloquearSlot(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{especialidade}/aguardando")
    public ResponseEntity<Long> contarAguardando(@PathVariable String especialidade) {
        long count = agendaService.contarPacientesAguardando(especialidade);
        return ResponseEntity.ok(count);
    }

}
