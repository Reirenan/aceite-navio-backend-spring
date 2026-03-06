package br.com.laps.aceite.api.vettings.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VettingResponse {

    private Long id;
    private String imo;
    private String reason;
    private LocalDateTime dataCreate;
    private String mmsi;
    private String nome;
    private String flag;
}
