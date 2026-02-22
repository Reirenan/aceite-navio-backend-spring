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

    private Double compriEstrutural;
    private Double compriUtil;
    private Double dwt;
    private Double largura;
    private Double profundidade;
    private Double caladoMax;
    private Double bocaMax;
    private Double loaMax;
    private String categoria;
    private List<String> accepts;

}
