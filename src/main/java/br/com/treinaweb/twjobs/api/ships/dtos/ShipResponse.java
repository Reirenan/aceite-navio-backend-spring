package br.com.treinaweb.twjobs.api.ships.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ShipResponse {

    private Long id;

    private String imo;
    
}
