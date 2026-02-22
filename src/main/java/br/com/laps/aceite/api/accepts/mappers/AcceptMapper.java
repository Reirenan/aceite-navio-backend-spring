package br.com.laps.aceite.api.accepts.mappers;

import br.com.laps.aceite.api.accepts.dtos.AcceptRequest;
import br.com.laps.aceite.api.accepts.dtos.AcceptResponse;
import br.com.laps.aceite.core.models.Accept;

public interface AcceptMapper {

    Accept toAccept(AcceptRequest acceptRequest);

    AcceptResponse toAcceptResponse(Accept accept);
}
