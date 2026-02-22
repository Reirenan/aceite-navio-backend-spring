package br.com.laps.aceite.api.navios.mappers;//package br.com.treinaweb.twjobs.api.ships.mappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModelMapperVesselMapper implements VesselMapper {

    private final ModelMapper modelMapper;

    @Override
    public Vessel toVessel(VesselRequest vesselRequest) {
        return modelMapper.map(vesselRequest, Vessel.class);
    }

    @Override
    public VesselResponse toVesselResponse(Vessel vessel) {
        return modelMapper.map(vessel, VesselResponse.class);
    }

}
