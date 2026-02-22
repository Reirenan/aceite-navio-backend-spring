package br.com.laps.aceite.api.accepts.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcceptRequest {

   // IMO é String no entity (7 dígitos)
   @NotEmpty(message = "IMO não pode estar vazio")
   @Size(min = 7, max = 7, message = "IMO deve ter exatamente 7 dígitos")
   private String imo;

   // Pode ser nulo
   private String obs;
   private String status;
   private String restricoes;

   // Enviar apenas IDs é mais correto
   private List<Long> bercosSelecionados;

   // ========================
   // Dados do Navio
   // ========================

   private String mmsi;

   private String nome;

   @NotNull(message = "LOA não pode ser nulo")
   @Positive(message = "LOA deve ser maior que zero")
   private Double loa;

   private Double boca;

   @NotNull(message = "DWT não pode ser nulo")
   @Positive(message = "DWT deve ser maior que zero")
   private Double dwt;

   private Double pontal;

   private Double ponteMfold;

   private Double mfoldQuilha;

   @NotEmpty(message = "Categoria não pode estar vazia")
   @Size(min = 1, max = 100, message = "Categoria deve ter no máximo 100 caracteres")
   private String categoria;

   private Integer flag;

   @NotNull(message = "Calado de entrada não pode ser nulo")
   @Positive(message = "Calado de entrada deve ser maior que zero")
   private Double caladoEntrada;

   @NotNull(message = "Calado de saída não pode ser nulo")
   @Positive(message = "Calado de saída deve ser maior que zero")
   private Double caladoSaida;
}