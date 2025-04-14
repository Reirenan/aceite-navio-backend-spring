package br.com.treinaweb.twjobs.api.aceites.dtos;

import br.com.treinaweb.twjobs.core.enums.AceiteStatus;
import br.com.treinaweb.twjobs.core.validators.VesselImoIsUnique;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AceiteRequest {

    private Long user;

    private Long vessel;

  //  @NotEmpty
    private Long imo;

  //  @NotEmpty
    private AceiteStatus status;



    
}
