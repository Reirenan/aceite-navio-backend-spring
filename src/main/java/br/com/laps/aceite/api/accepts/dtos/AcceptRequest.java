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
}