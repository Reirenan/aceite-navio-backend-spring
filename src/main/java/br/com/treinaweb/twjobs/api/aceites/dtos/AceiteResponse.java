package br.com.treinaweb.twjobs.api.aceites.dtos;

import br.com.treinaweb.twjobs.core.enums.AceiteStatus;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AceiteResponse {

    private Long id;
    private String user;
    private String vessel;
    private Long imo;
    private AceiteStatus status;
    
}
