package br.com.laps.aceite.api.vettings.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VettingRequest {

    @NotBlank
    @Size(min = 7, max = 7)
    private String imo;

    @NotBlank
    @Size(max = 2000)
    private String reason;

    private String mmsi;

    @Size(max = 150)
    private String nome;

    private String flag;
}
