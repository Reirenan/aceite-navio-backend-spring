package br.com.treinaweb.twjobs.api.accepts.mappers;//package br.com.treinaweb.twjobs.api.ships.mappers;

import br.com.treinaweb.twjobs.api.accepts.dtos.AcceptRequest;
import br.com.treinaweb.twjobs.api.accepts.dtos.AcceptResponse;
import br.com.treinaweb.twjobs.core.models.Accept;
import br.com.treinaweb.twjobs.core.models.Aceite;

//
//import br.com.treinaweb.twjobs.api.ships.dtos.ShipRequest;
//import br.com.treinaweb.twjobs.api.ships.dtos.ShipResponse;
//import br.com.treinaweb.twjobs.core.models.Skill;
//
public interface AcceptMapper {
//

    Accept toAccept(AcceptRequest acceptRequest);

    AcceptResponse toAcceptResponse(Accept accept);
//
}
