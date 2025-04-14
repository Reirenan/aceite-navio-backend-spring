package br.com.treinaweb.twjobs.api.vessels.mappers;//package br.com.treinaweb.twjobs.api.ships.mappers;

import br.com.treinaweb.twjobs.api.ships.dtos.ShipRequest;
import br.com.treinaweb.twjobs.api.ships.dtos.ShipResponse;
import br.com.treinaweb.twjobs.api.vessels.dtos.VesselRequest;
import br.com.treinaweb.twjobs.api.vessels.dtos.VesselResponse;
import br.com.treinaweb.twjobs.core.models.Skill;
import br.com.treinaweb.twjobs.core.models.Vessel;
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
