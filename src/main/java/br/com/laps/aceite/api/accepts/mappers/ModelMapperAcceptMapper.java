package br.com.laps.aceite.api.accepts.mappers;


import br.com.laps.aceite.api.accepts.dtos.AcceptRequest;
import br.com.laps.aceite.api.accepts.dtos.AcceptResponse;
import br.com.laps.aceite.core.models.Accept;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ModelMapperAcceptMapper implements AcceptMapper {

    private final ModelMapper modelMapper;

    @Override
    public AcceptResponse toAcceptResponse(Accept accept) {
        return modelMapper.map(accept, AcceptResponse.class);
    }

    @Override
    public Accept toAccept(@Valid AcceptRequest acceptRequest) {
        return modelMapper.map(acceptRequest, Accept.class);
    }

}

