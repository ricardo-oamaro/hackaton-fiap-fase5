package br.com.fiap.hackaton.service;

import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class TriagemService {
    public String calcularPrioridade(String sintomas) {
        if (StringUtils.isBlank(sintomas)) return "Baixa";
        var s = sintomas.toLowerCase();
        if (s.contains("dor no peito") || s.contains("desmaio") || s.contains("falta de ar")) return "Alta";
        if (s.contains("febre") || s.contains("infecção") || s.contains("queda")) return "Media";
        return "Baixa";
    }

    // Para ordenação: Alta > Media > Baixa
    public int prioridadeRank(String prioridade) {
        return switch (prioridade) {
            case "Alta" -> 3;
            case "Media" -> 2;
            default -> 1;
        };
    }
}
