package br.com.laps.aceite.api.accepts.dtos;

import br.com.laps.aceite.core.enums.AceiteStatus;
import br.com.laps.aceite.core.models.Berco;
import br.com.laps.aceite.core.models.Vessel;
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

    // Referências (igual ao Accept: User e Vessel)

    // IMO (7 dígitos) no entity é String
    private String imo;

    private String categoria;
    private Double loa;
    private Double dwt;
    private Double calado_entrada;
    private Double calado_saida;

    // Enum igual ao entity
    private AceiteStatus status;

    private String obs;
    private String restricoes;

    // data_accept no entity é LocalDateTime (dataHoraAccept)
    private LocalDateTime dataHoraAccept;

    // Berços
    private List<Berco> bercos;

    private Long userId;
    private String agenteNome;

    private Long vesselId;
    private String nome;
    private String mmsi;
    private Double boca;
    private Double pontal;
    private String flag;
    private Double calado_max;

    private Double ponte_mfold;
    private Double mfold_quilha;

    private String codigo;
    private String path;
}