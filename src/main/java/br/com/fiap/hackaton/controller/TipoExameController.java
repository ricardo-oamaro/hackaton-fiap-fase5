package br.com.fiap.hackaton.controller;

import br.com.fiap.hackaton.dto.TipoExameRequestDto;
import br.com.fiap.hackaton.dto.TipoExameResponseDto;
import br.com.fiap.hackaton.service.TipoExameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tipos-exame")
@RequiredArgsConstructor
public class TipoExameController {

    private final TipoExameService tipoExameService;

    @PostMapping
    public ResponseEntity<TipoExameResponseDto> criar(@Valid @RequestBody TipoExameRequestDto request) {
        var response = tipoExameService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoExameResponseDto> atualizar(@PathVariable Long id,
                                                          @Valid @RequestBody TipoExameRequestDto request) {
        var response = tipoExameService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TipoExameResponseDto>> listarAtivos() {
        var response = tipoExameService.listarAtivos();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoExameResponseDto> buscarPorId(@PathVariable Long id) {
        var response = tipoExameService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<TipoExameResponseDto>> buscar(@RequestParam String texto) {
        var response = tipoExameService.buscar(texto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        tipoExameService.inativar(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        tipoExameService.ativar(id);
        return ResponseEntity.ok().build();
    }
}