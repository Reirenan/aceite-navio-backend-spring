package br.com.treinaweb.twjobs.api.aceites.mappers;//package br.com.treinaweb.twjobs.api.ships.mappers;

import br.com.treinaweb.twjobs.api.aceites.dtos.AceiteRequest;
import br.com.treinaweb.twjobs.api.aceites.dtos.AceiteResponse;
import br.com.treinaweb.twjobs.api.jobs.dtos.JobRequest;
import br.com.treinaweb.twjobs.core.models.Aceite;
import jakarta.validation.Valid;

//
//import br.com.treinaweb.twjobs.api.ships.dtos.ShipRequest;
//import br.com.treinaweb.twjobs.api.ships.dtos.ShipResponse;
//import br.com.treinaweb.twjobs.core.models.Skill;
//
public interface AceiteMapper {
//
    AceiteResponse toAceiteResponse(Aceite aceite);
    Aceite toAceite(AceiteRequest aceiteRequest);
//
}
