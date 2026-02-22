package br.com.treinaweb.twjobs.api.bercos.mappers;//package br.com.treinaweb.twjobs.api.ships.mappers;

import br.com.treinaweb.twjobs.api.bercos.dtos.BercoRequest;
import br.com.treinaweb.twjobs.api.bercos.dtos.BercoResponse;
import br.com.treinaweb.twjobs.core.models.Berco;
import br.com.treinaweb.twjobs.core.models.Vessel;

public interface BercoMapper {

    Berco toBerco(BercoRequest bercoRequest);
    BercoResponse toBercoResponse(Berco berco);

}
