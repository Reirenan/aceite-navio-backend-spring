package br.com.laps.aceite.api.navios.mappers;//package br.com.treinaweb.twjobs.api.ships.mappers;



public interface VesselMapper {

    Vessel toVessel(VesselRequest vesselRequest);
    VesselResponse toVesselResponse(Vessel vessel);

}
