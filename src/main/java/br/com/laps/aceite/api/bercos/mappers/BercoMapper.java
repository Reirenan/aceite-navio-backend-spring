package br.com.laps.aceite.api.bercos.mappers;//package br.com.treinaweb.twjobs.api.ships.mappers;

import br.com.laps.aceite.api.bercos.dtos.BercoRequest;
import br.com.laps.aceite.api.bercos.dtos.BercoResponse;
import br.com.laps.aceite.core.models.Berco;

public interface BercoMapper {

    Berco toBerco(BercoRequest bercoRequest);
    BercoResponse toBercoResponse(Berco berco);

}
