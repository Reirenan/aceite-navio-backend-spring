package br.com.treinaweb.twjobs.api.blackList.dtos;

import br.com.treinaweb.twjobs.core.enums.AceiteStatus;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BlackListResponse {
    private Long id;
    private Long imo;

    private String reason;

    private String data_blacklisted;
    private String data_create;
    private String data_update;

    private String time_blacklisted;
    private String time_create;
    private String time_update;

//    private List<Long> bercos;
//private List<BercoRequestAceite> bercos;

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
}
