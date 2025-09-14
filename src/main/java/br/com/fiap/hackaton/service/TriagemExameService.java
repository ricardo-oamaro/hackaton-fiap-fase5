package br.com.fiap.hackaton.service;

import br.com.fiap.hackaton.model.TipoExame;
import org.springframework.stereotype.Service;
import io.micrometer.common.util.StringUtils;
import java.time.OffsetDateTime;
import java.util.Arrays;

@Service
public class TriagemExameService {

    public String calcularPrioridadeExame(TipoExame tipoExame, String observacoes, boolean urgente, String justificativa) {
        // Se marcado como urgente ou tipo naturalmente urgente
        if (urgente || tipoExame.getUrgentePorPadrao()) {
            return "Alta";
        }

        // Analise das observacoes usando palavras-chave configuraveis
        if (!StringUtils.isBlank(observacoes)) {
            String obs = observacoes.toLowerCase();

            // Verificar palavras-chave para alta prioridade
            if (!StringUtils.isBlank(tipoExame.getPalavrasChaveAlta())) {
                boolean temPalavraAlta = Arrays.stream(tipoExame.getPalavrasChaveAlta().split(","))
                        .map(String::trim)
                        .map(String::toLowerCase)
                        .anyMatch(obs::contains);
                if (temPalavraAlta) return "Alta";
            }

            // Verificar palavras-chave para média prioridade
            if (!StringUtils.isBlank(tipoExame.getPalavrasChaveMedia())) {
                boolean temPalavraMedia = Arrays.stream(tipoExame.getPalavrasChaveMedia().split(","))
                        .map(String::trim)
                        .map(String::toLowerCase)
                        .anyMatch(obs::contains);
                if (temPalavraMedia) return "Media";
            }
        }

        return "Baixa";
    }

    public String gerarJustificativa(TipoExame tipoExame, String prioridade, String observacoes, boolean urgente) {
        if ("Alta".equals(prioridade)) {
            if (urgente) return "Exame marcado como urgente";
            if (tipoExame.getUrgentePorPadrao()) return "Tipo de exame requer prioridade alta";
            return "Baseado nas observações clínicas";
        }

        if ("Media".equals(prioridade)) {
            return "Exame de acompanhamento ou tipo que requer atenção moderada";
        }

        return "Exame de rotina";
    }

    public int obterRankingPrioridade(String prioridade) {
        return switch (prioridade) {
            case "Alta" -> 3;
            case "Media" -> 2;
            default -> 1;
        };
    }

    // Metodo único para calcular data limite
    public OffsetDateTime calcularDataLimite(TipoExame tipoExame, String prioridade) {
        OffsetDateTime agora = OffsetDateTime.now();
        return switch (prioridade) {
            case "Alta" -> agora.plusDays(tipoExame.getPrazoDiasAlta());
            case "Media" -> agora.plusDays(tipoExame.getPrazoDiasMedia());
            default -> agora.plusDays(tipoExame.getPrazoDiasBaixa());
        };
    }
}