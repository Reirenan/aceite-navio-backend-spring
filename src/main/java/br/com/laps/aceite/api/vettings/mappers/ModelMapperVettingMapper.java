package br.com.laps.aceite.api.vettings.mappers;

import br.com.laps.aceite.api.vettings.dtos.VettingRequest;
import br.com.laps.aceite.api.vettings.dtos.VettingResponse;
import br.com.laps.aceite.core.models.Vetting;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModelMapperVettingMapper implements VettingMapper {

    private final ModelMapper modelMapper;

    @Override
    public Vetting toVetting(VettingRequest vettingRequest) {
        return modelMapper.map(vettingRequest, Vetting.class);
    }

    @Override
    public VettingResponse toVettingResponse(Vetting vetting) {
        return modelMapper.map(vetting, VettingResponse.class);
    }

}
