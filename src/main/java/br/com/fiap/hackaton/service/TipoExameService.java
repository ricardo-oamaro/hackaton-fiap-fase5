package br.com.fiap.hackaton.service;

import br.com.fiap.hackaton.dto.TipoExameRequestDto;
import br.com.fiap.hackaton.dto.TipoExameResponseDto;
import br.com.fiap.hackaton.model.TipoExame;
import br.com.fiap.hackaton.repository.TipoExameRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoExameService {

    private final TipoExameRepository tipoExameRepository;

    @Transactional
    public TipoExameResponseDto criar(TipoExameRequestDto request) {
        if (tipoExameRepository.existsByCodigo(request.getCodigo())) {
            throw new IllegalArgumentException("Já existe um tipo de exame com esse código");
        }

        var tipoExame = TipoExame.builder()
                .codigo(request.getCodigo().toUpperCase())
                .descricao(request.getDescricao())
                .urgentePorPadrao(request.getUrgentePorPadrao())
                .ativo(true)
                .duracaoMinutos(request.getDuracaoMinutos())
                .observacoes(request.getObservacoes())
                .palavrasChaveAlta(request.getPalavrasChaveAlta())
                .palavrasChaveMedia(request.getPalavrasChaveMedia())
                .prazoDiasAlta(request.getPrazoDiasAlta())
                .prazoDiasMedia(request.getPrazoDiasMedia())
                .prazoDiasBaixa(request.getPrazoDiasBaixa())
                .build();

        tipoExame = tipoExameRepository.save(tipoExame);
        return mapearParaDto(tipoExame);
    }

    @Transactional
    public TipoExameResponseDto atualizar(Long id, TipoExameRequestDto request) {
        TipoExame tipoExame = tipoExameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de exame não encontrado"));

        // Verificar se codigo ja existe em outro registro
        var existente = tipoExameRepository.findByCodigo(request.getCodigo().toUpperCase());
        if (existente.isPresent() && !existente.get().getId().equals(id)) {
            throw new IllegalArgumentException("Já existe outro tipo de exame com esse código");
        }

        tipoExame.setCodigo(request.getCodigo().toUpperCase());
        tipoExame.setDescricao(request.getDescricao());
        tipoExame.setUrgentePorPadrao(request.getUrgentePorPadrao());
        tipoExame.setDuracaoMinutos(request.getDuracaoMinutos());
        tipoExame.setObservacoes(request.getObservacoes());
        tipoExame.setPalavrasChaveAlta(request.getPalavrasChaveAlta());
        tipoExame.setPalavrasChaveMedia(request.getPalavrasChaveMedia());
        tipoExame.setPrazoDiasAlta(request.getPrazoDiasAlta());
        tipoExame.setPrazoDiasMedia(request.getPrazoDiasMedia());
        tipoExame.setPrazoDiasBaixa(request.getPrazoDiasBaixa());

        tipoExame = tipoExameRepository.save(tipoExame);
        return mapearParaDto(tipoExame);
    }

    @Transactional(readOnly = true)
    public List<TipoExameResponseDto> listarAtivos() {
        return tipoExameRepository.findByAtivoTrueOrderByDescricao()
                .stream()
                .map(this::mapearParaDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public TipoExameResponseDto buscarPorId(Long id) {
        var tipoExame = tipoExameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de exame não encontrado"));
        return mapearParaDto(tipoExame);
    }

    @Transactional(readOnly = true)
    public List<TipoExameResponseDto> buscar(String texto) {
        return tipoExameRepository.buscarPorTexto(texto)
                .stream()
                .map(this::mapearParaDto)
                .toList();
    }

    @Transactional
    public void inativar(Long id) {
        var tipoExame = tipoExameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de exame não encontrado"));

        tipoExame.setAtivo(false);
        tipoExameRepository.save(tipoExame);
    }

    @Transactional
    public void ativar(Long id) {
        var tipoExame = tipoExameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de exame não encontrado"));
        tipoExame.setAtivo(true);
        tipoExameRepository.save(tipoExame);
    }

    private TipoExameResponseDto mapearParaDto(TipoExame tipoExame) {
        return TipoExameResponseDto.builder()
                .id(tipoExame.getId())
                .codigo(tipoExame.getCodigo())
                .descricao(tipoExame.getDescricao())
                .urgentePorPadrao(tipoExame.getUrgentePorPadrao())
                .ativo(tipoExame.getAtivo())
                .duracaoMinutos(tipoExame.getDuracaoMinutos())
                .observacoes(tipoExame.getObservacoes())
                .palavrasChaveAlta(tipoExame.getPalavrasChaveAlta())
                .palavrasChaveMedia(tipoExame.getPalavrasChaveMedia())
                .prazoDiasAlta(tipoExame.getPrazoDiasAlta())
                .prazoDiasMedia(tipoExame.getPrazoDiasMedia())
                .prazoDiasBaixa(tipoExame.getPrazoDiasBaixa())
                .criadoEm(tipoExame.getCriadoEm())
                .atualizadoEm(tipoExame.getAtualizadoEm())
                .build();
    }
}