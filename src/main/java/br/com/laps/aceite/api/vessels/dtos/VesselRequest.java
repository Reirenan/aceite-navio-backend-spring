package br.com.laps.aceite.api.navios.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VesselRequest {

    @NotNull(message = "IMO não pode ser nulo")
    private Long imo;

    private String mmsi;

    private String nome;

    @NotNull(message = "LOA deve ser maior que zero")
    private Float loa;

    private Float boca;

    @NotNull(message = "DWT deve ser maior que zero")
    private Float dwt;

    private Float pontal;

    private Float ponte_mfold;

    private Float mfold_quilha;

    @NotNull(message = "CALADO MAXIMO deve ser maior que zero")
    private Float calado_max;

    private String categoria;

    @JsonProperty("flag")
    private String flag;

    private String obs;

    private String path;
}