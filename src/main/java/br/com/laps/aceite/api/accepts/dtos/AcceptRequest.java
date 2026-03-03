package br.com.laps.aceite.api.accepts.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcceptRequest {

   @NotEmpty(message = "IMO não pode estar vazio")
   @Size(min = 7, max = 7, message = "IMO deve ter exatamente 7 dígitos")
   private String imo;

   private String mmsi;
   private String nome;

   private Double loa;
   private Double boca;
   private Double dwt;
   private Double pontal;
   private Double calado_entrada;
   private Double calado_saida;
   private Double calado_max;

   private String categoria;
   private String flag;

   private Double ponte_mfold;
   private Double mfold_quilha;

   private String obs;
   private AceiteStatus status;
   private List<Long> bercosSelecionados;
}