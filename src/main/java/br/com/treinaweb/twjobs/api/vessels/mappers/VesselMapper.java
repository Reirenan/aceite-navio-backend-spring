package br.com.treinaweb.twjobs.api.vessels.mappers;//package br.com.treinaweb.twjobs.api.ships.mappers;

import br.com.treinaweb.twjobs.api.ships.dtos.ShipRequest;
import br.com.treinaweb.twjobs.api.ships.dtos.ShipResponse;
import br.com.treinaweb.twjobs.api.vessels.dtos.VesselRequest;
import br.com.treinaweb.twjobs.api.vessels.dtos.VesselResponse;
import br.com.treinaweb.twjobs.core.models.Skill;
import br.com.treinaweb.twjobs.core.models.Vessel;

public interface VesselMapper {

    Vessel toVessel(VesselRequest vesselRequest);
    VesselResponse toVesselResponse(Vessel vessel);

}
