package br.com.laps.aceite.api.vessels.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class VesselResponse {

    private Long id;
    private String user;
    private Long imo;
    private String mmsi;
    private String nome;
    private Float loa;
    private Float calado_max;
    private Float calado_entrada;
    private Float calado_saida;
    private Float boca;
    private Float dwt;
    private Float pontal;
    private Float ponte_mfold;
    private Float mfold_quilha;
    private String categoria;
    private String flag;
    private String obs;
    private String status;
    private String st_ver_vessel;
    private String path;

}
