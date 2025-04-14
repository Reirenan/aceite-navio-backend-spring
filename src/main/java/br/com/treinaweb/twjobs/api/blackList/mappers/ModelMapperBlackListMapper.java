package br.com.treinaweb.twjobs.api.blackList.mappers;


import br.com.treinaweb.twjobs.api.blackList.dtos.BlackListRequest;
import br.com.treinaweb.twjobs.api.blackList.dtos.BlackListResponse;
import br.com.treinaweb.twjobs.core.models.Accept;
import br.com.treinaweb.twjobs.core.models.BlackList;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class ModelMapperBlackListMapper implements BlackListMapper {

    private final ModelMapper modelMapper;

    @Override
    public BlackListResponse toBlackListResponse(BlackList blackList) {
        return modelMapper.map(blackList, BlackListResponse.class);
    }

    @Override
    public BlackList toBlackList(@Valid BlackListRequest blackListRequest) {
        return modelMapper.map(blackListRequest, BlackList.class);
    }

}

