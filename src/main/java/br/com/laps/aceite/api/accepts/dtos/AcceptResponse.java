package br.com.laps.aceite.api.accepts.dtos;

import br.com.laps.aceite.core.enums.AceiteStatus;
import br.com.laps.aceite.core.models.Berco;
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
    private Long userId;
    private Long vesselId;

    // IMO (7 dígitos) no entity é String
    private String imo;

    // Enum igual ao entity
    private AceiteStatus status;

    private String obs;
    private String restricoes;

    // data_accept no entity é LocalDateTime (dataHoraAccept)
    private LocalDateTime dataHoraAccept;

    // Berços
    private List<Berco> bercos;

    private String codigo;
    private String path;
}