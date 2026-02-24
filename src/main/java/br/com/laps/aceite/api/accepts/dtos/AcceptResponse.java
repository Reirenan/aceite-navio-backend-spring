package br.com.laps.aceite.api.accepts.dtos;

import br.com.laps.aceite.core.enums.AceiteStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AcceptResponse {

    private Long id;

    // Referências
    private Long userId;
    private String agenteNome; // Nome do Agente que solicitou
    private Long vesselId;

    // Dados do Navio
    private String imo;
    private String nome;
    private String mmsi;
    private Double loa;
    private Double boca;
    private Double dwt;
    private Double pontal;
    private Double calado_max;
    private Double caladoEntrada;
    private Double caladoSaida;
    private String categoria;
    private String flag;

    // Dados do Aceite
    private AceiteStatus status;
    private String obs;
    private String restricoes;
    private Double ponte_mfold;
    private Double mfold_quilha;
    private LocalDateTime dataHoraAccept;
    private String codigo;
    private String path;

    // Berços (Ids ou objetos simplificados)
    private List<Long> bercosSelecionados;
}