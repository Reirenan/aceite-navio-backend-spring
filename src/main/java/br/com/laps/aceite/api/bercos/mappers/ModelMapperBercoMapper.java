package br.com.treinaweb.twjobs.api.bercos.mappers;//package br.com.treinaweb.twjobs.api.ships.mappers;

import br.com.treinaweb.twjobs.api.bercos.dtos.BercoRequest;
import br.com.treinaweb.twjobs.api.bercos.dtos.BercoResponse;
import br.com.treinaweb.twjobs.core.models.Berco;
import br.com.treinaweb.twjobs.core.models.Vessel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModelMapperBercoMapper implements BercoMapper {

    private final ModelMapper modelMapper;

    @Override
    public Berco toBerco(BercoRequest bercoRequest) {
        return modelMapper.map(bercoRequest, Berco.class);
    }

    @Override
    public BercoResponse toBercoResponse(Berco berco) {
        return modelMapper.map(berco, BercoResponse.class);
    }

}
