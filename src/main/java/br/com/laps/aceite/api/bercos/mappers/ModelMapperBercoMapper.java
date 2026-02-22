package br.com.laps.aceite.api.bercos.mappers;//package br.com.laps.aceite.api.ships.mappers;

import br.com.laps.aceite.api.bercos.dtos.BercoRequest;
import br.com.laps.aceite.api.bercos.dtos.BercoResponse;
import br.com.laps.aceite.core.models.Berco;
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
