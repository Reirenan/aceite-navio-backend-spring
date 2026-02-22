package br.com.treinaweb.twjobs.api.bercos.dtos;

import br.com.treinaweb.twjobs.core.models.Accept;
import br.com.treinaweb.twjobs.core.validators.VesselImoIsUnique;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BercoRequest {

    //   AJEITAR

//    private Long user;
//not empty não está funcionando. Ajeiter.
//    @NotEmpty

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
//    private List<Long> accepts; //Novo request que não possua essa linha.
    private List<Accept> accepts;
}
