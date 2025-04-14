package br.com.treinaweb.twjobs.api.blackList.dtos;

import br.com.treinaweb.twjobs.core.models.Berco;
import br.com.treinaweb.twjobs.core.validators.VesselImoIsUnique;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlackListRequest {

   //   AJEITAR

//   @VesselImoIsUnique
   @NotNull(message = "IMO deve ter no mínimo 7 caracteres")
//   @Size(min = 7, message = "IMO deve ter no mínimo 7 caracteres")
   private Long imo;

   @NotEmpty(message = "MOTIVO não deve estar vazio.")
   private String reason;

//   private String data_blacklisted;
//   private String data_create;
//   private String data_update;
//
//   private String time_blacklisted;
//   private String time_create;
//   private String time_update;

   //    Dados navio

   private String mmsi="Null";

   private String nome="Null";

  // @NotNull(message = "LOA deve ser maior que zero")
//   @Size(min = 1, max = 10, message = "Calado deve ter no mínimo 1 e no máximo 10 caracteres")
   private Float loa=0.0F;

   private Float boca=0.0F;

   // @NotNull(message = "DWT deve ser maior que zero")
//   @Size(value=1 , message = "Calado deve ter no mínimo 1 e no máximo 10 caracteres")
   private Float dwt=0.0F;

   private Float pontal=0.0F;

   private Float ponte_mfold=0.0F;

   private Float mfold_quilha=0.0F;

   // @NotEmpty
   // @Size(min = 1, max = 100, message = "CATEGORIA deve ter no mínimo 1 e no máximo 100 caracteres")
   private String categoria="Null";

   private Integer flag=0;

   //@NotNull(message = "calado deve ser maior que zero")
//   @Size(min = 1, max = 10, message = "Calado deve ter no mínimo 1 e no máximo 10 caracteres")
   private Float calado_entrada=0.0F;

   //@NotNull(message = "calado deve ser maior que zero")
//   @Size(min = 1, max = 10, message = "Calado deve ter no mínimo 1 e no máximo 10 caracteres")
   private Float calado_saida=0.0F;

    
}
