package br.com.treinaweb.twjobs.api.accepts.dtos;

import br.com.treinaweb.twjobs.core.enums.VeriStatus;
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
public class AcceptRequest {

//   NOT EMPTY SÒ COM STRING
//   COM NUMEROS È O NOTNULL

//   AJEITAR

//   @VesselImoIsUnique
   @NotNull(message = "IMO não pode ser nulo")
//   @Size(min = 7, message = "IMO deve ter no mínimo 7 caracteres")
   private Long imo;


   //CAN BE NULL
   private String obs;
   private String status;
   private String restricoes;

   //CAN BE NULL
   private List<Berco> bercos;

// ~~~   PODE SER NULO
   private List<Long> bercosId;

//   @NotEmpty(message = "data não deve estar vazia")
//   private String dataAccept;
//   @NotEmpty(message = "data não deve estar vazia")
//   private String data_create;
//   @NotEmpty(message = "data não deve estar vazia")
//   private String data_update;

   //    Dados navio

   private String mmsi;

   private String nome;

   @NotNull(message = "LOA deve ser maior que zero")
//   @Size(min = 1, max = 10, message = "Calado deve ter no mínimo 1 e no máximo 10 caracteres")
   private Float loa;

   private Float boca;

   @NotNull(message = "DWT deve ser maior que zero")
//   @Size(value=1 , message = "Calado deve ter no mínimo 1 e no máximo 10 caracteres")
   private Float dwt;

   private Float pontal;

   private Float ponte_mfold;

   private Float mfold_quilha;

   @NotEmpty
   @Size(min = 1, max = 100, message = "CATEGORIA deve ter no mínimo 1 e no máximo 100 caracteres")
   private String categoria;

   private Integer flag;

   @NotNull(message = "calado deve ser maior que zero")
//   @Size(min = 1, max = 10, message = "Calado deve ter no mínimo 1 e no máximo 10 caracteres")
   private Float calado_entrada;

   @NotNull(message = "calado deve ser maior que zero")
//   @Size(min = 1, max = 10, message = "Calado deve ter no mínimo 1 e no máximo 10 caracteres")
   private Float calado_saida;

   private List<Long> bercosSelecionados;

    
}
