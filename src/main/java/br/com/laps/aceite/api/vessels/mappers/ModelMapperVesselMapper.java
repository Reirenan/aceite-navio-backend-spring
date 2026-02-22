package br.com.laps.aceite.api.vessels.mappers;//package br.com.treinaweb.twjobs.api.ships.mappers;

import br.com.laps.aceite.api.vessels.dtos.VesselRequest;
import br.com.laps.aceite.api.vessels.dtos.VesselResponse;
import br.com.laps.aceite.core.models.Vessel;
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
