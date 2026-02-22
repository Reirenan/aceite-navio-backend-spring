package br.com.laps.aceite.api.bercos.dtos;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BercoResponse {

    private Long id;

    private String nome;

    private Double compri_estrutural;
    private Double compri_util;
    private Double dwt;
    private Double largura;
    private Double profundidade;
    private Double calado_max;
    private Double boca_max;
    private Double loa_max;
    private String categoria;
    private List<String> accepts;
    
}
