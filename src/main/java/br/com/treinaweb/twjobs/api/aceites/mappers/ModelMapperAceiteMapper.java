package br.com.treinaweb.twjobs.api.aceites.mappers;


import br.com.treinaweb.twjobs.api.aceites.dtos.AceiteRequest;
import br.com.treinaweb.twjobs.api.aceites.dtos.AceiteResponse;
import br.com.treinaweb.twjobs.api.jobs.dtos.JobRequest;
import br.com.treinaweb.twjobs.core.models.Aceite;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class ModelMapperAceiteMapper implements AceiteMapper {

    private final ModelMapper modelMapper;

    @Override
    public AceiteResponse toAceiteResponse(Aceite aceite) {
        return modelMapper.map(aceite, AceiteResponse.class);
    }

    @Override
    public Aceite toAceite(@Valid AceiteRequest aceiteRequest) {
        return modelMapper.map(aceiteRequest, Aceite.class);
    }

}

