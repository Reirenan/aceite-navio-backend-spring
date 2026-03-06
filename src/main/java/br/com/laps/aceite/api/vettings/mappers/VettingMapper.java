package br.com.laps.aceite.api.vettings.mappers;

import br.com.laps.aceite.api.vettings.dtos.VettingRequest;
import br.com.laps.aceite.api.vettings.dtos.VettingResponse;
import br.com.laps.aceite.core.models.Vetting;

public interface VettingMapper {

    Vetting toVetting(VettingRequest vettingRequest);

    VettingResponse toVettingResponse(Vetting vetting);

}
