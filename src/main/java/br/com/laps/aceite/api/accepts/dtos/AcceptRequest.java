package br.com.laps.aceite.api.accepts.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcceptRequest {

   private Long vesselId;
   // IMO é String no entity (7 dígitos)
   @NotEmpty(message = "IMO não pode estar vazio")
   @Size(min = 7, max = 7, message = "IMO deve ter exatamente 7 dígitos")
   private String imo;

   // Dados do Navio (Snapshot)
   private String nome;
   private String mmsi;
   private Double loa;
   private Double boca;
   private Double dwt;
   private Double pontal;
   private Double calado_max;
   private String categoria;
   private String flag;

   // Dados específicos do Aceite
   private Double caladoEntrada;
   private Double caladoSaida;

   // Outros dados
   private String obs;
   private String status;
   private String restricoes;
   private Double ponte_mfold;
   private Double mfold_quilha;

   // Enviar apenas IDs é mais correto
   private List<Long> bercosSelecionados;
}