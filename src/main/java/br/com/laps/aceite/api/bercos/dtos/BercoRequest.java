package br.com.laps.aceite.api.bercos.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BercoRequest {

    @NotBlank
    private String nome;

    @NotNull
    private Double compriEstrutural;

    @NotNull
    private Double compriUtil;

    @NotNull
    private Double dwt;

    @NotNull
    private Double largura;

    @NotNull
    private Double profundidade;

    @NotNull
    private Double caladoMax;

    @NotNull
    private Double bocaMax;

    @NotNull
    private Double loaMax;

    @NotBlank
    private String categoria;

    //private List<Accept> accepts;
}