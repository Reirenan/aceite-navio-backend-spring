package br.com.laps.aceite.api.vessels.mappers;//package br.com.treinaweb.twjobs.api.ships.mappers;


import br.com.laps.aceite.api.vessels.dtos.VesselRequest;
import br.com.laps.aceite.api.vessels.dtos.VesselResponse;
import br.com.laps.aceite.core.models.Vessel;

public interface VesselMapper {

    Vessel toVessel(VesselRequest vesselRequest);
    VesselResponse toVesselResponse(Vessel vessel);

}
