package br.com.treinaweb.twjobs.api.bercos.dtos;

import br.com.treinaweb.twjobs.core.models.Accept;
import br.com.treinaweb.twjobs.core.models.Berco;
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

    private Long compri_estrutural;
    private Long compri_util;
    private Float dwt;
    private Long largura;
    private Long profundidade;
    private Float calado_max;
    private Float boca_max;
    private Float loa_max;
    private String categoria;
    private List<String> accepts;
    
}
