package br.com.treinaweb.twjobs.api.vessels.dtos;

import br.com.treinaweb.twjobs.core.enums.VeriStatus;
import br.com.treinaweb.twjobs.core.validators.SkillNameIsUnique;
import br.com.treinaweb.twjobs.core.validators.VesselImoIsUnique;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VesselRequest {
    //   AJEITAR

    //COMENTEI ID, SE DER ERRO É ISSO AQUI
    //private Long id;
//    private Long user;
//not empty não está funcionando. Ajeiter.
//    @NotEmpty

//    @VesselImoIsUnique
    @NotNull(message = "IMO não pode ser nulo")
//    @Size(min = 7, message = "IMO deve ter no mínimo 7 caracteres")
    private Long imo;

    private String mmsi;

    private String nome;

    //NOT NULL AND GREATER THAN ZERO
    @NotNull(message = "LOA deve ser maior que zero")
//    @Size(min = 1, max = 100, message = "LOA deve ter no mínimo 1 e no máximo 100 caracteres")
    private Float loa;

    private Float boca;

    //NOT NULL AND GREATER THAN ZERO
    @NotNull(message = "DWT deve ser maior que zero")
//    @Size(min = 1, max = 100, message = "DWT deve ter no mínimo 1 e no máximo 100 caracteres")
    private Float dwt;

    private Float pontal;

    private Float ponte_mfold;

    private Float mfold_quilha;

    @NotNull(message = "CALADO MAXIMO deve ser maior que zero")
    private Float calado_max;


    //NOT NULL
//    @NotEmpty
//    @Size(min = 1, max = 100, message = "CATEGORIA deve ter no mínimo 1 e no máximo 100 caracteres")
    private String categoria;

    @JsonProperty("flag")
    private String flag;

    private String obs;

    //private VeriStatus status;

    //private VeriStatus st_ver_vessel;

    private String path;
}
