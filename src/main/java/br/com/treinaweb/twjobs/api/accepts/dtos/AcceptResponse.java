package br.com.treinaweb.twjobs.api.accepts.dtos;

import br.com.treinaweb.twjobs.core.enums.AceiteStatus;
import br.com.treinaweb.twjobs.core.enums.CategoriaVessel;
import br.com.treinaweb.twjobs.core.models.Berco;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AcceptResponse {

    private Long id;
    private String user;
    private String vessel;
    private Long imo;
    private String status;
    private String obs;
    private String restricoes;
    private String dataAccept;
    private String data_create;
    private String data_update;

    private String time_accept;
    private String time_create;
    private String time_update;

    private List<Berco> bercos;

    private String path;



    //    Dados navio

    private String mmsi;

    private String nome;

    private Float loa;

    private Float boca;

    private Float dwt;

    private Float pontal;

    private Float ponte_mfold;

    private Float mfold_quilha;

    private String categoria;

    private Integer flag;


    private Float calado_entrada;

    private Float calado_saida;
    private List<Long> bercosSelecionados;


//    private List<Long> bercos;
//private List<BercoRequestAceite> bercos;
}
