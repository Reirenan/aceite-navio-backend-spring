package br.com.treinaweb.twjobs.api.blackList.mappers;//package br.com.treinaweb.twjobs.api.ships.mappers;

import br.com.treinaweb.twjobs.api.blackList.dtos.BlackListRequest;
import br.com.treinaweb.twjobs.api.blackList.dtos.BlackListResponse;
import br.com.treinaweb.twjobs.core.models.Accept;
import br.com.treinaweb.twjobs.core.models.BlackList;

//
//import br.com.treinaweb.twjobs.api.ships.dtos.ShipRequest;
//import br.com.treinaweb.twjobs.api.ships.dtos.ShipResponse;
//import br.com.treinaweb.twjobs.core.models.Skill;
//
public interface BlackListMapper {
//

    BlackList toBlackList(BlackListRequest blackListRequest);

    BlackListResponse toBlackListResponse(BlackList blackList);
//
}
