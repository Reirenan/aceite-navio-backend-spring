package br.com.laps.aceite.api.accepts.mappers;

import br.com.laps.aceite.api.accepts.dtos.AcceptRequest;
import br.com.laps.aceite.api.accepts.dtos.AcceptResponse;
import br.com.laps.aceite.core.models.Accept;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ModelMapperAcceptMapper implements AcceptMapper {

    private final ModelMapper modelMapper;

    public ModelMapperAcceptMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        // Configuração: AcceptRequest -> Accept
        this.modelMapper.typeMap(AcceptRequest.class, Accept.class).addMappings(mapper -> {
            // Campos com nomes idênticos são mapeados automaticamente agora
        });

        // Configuração: Accept -> AcceptResponse
        this.modelMapper.typeMap(Accept.class, AcceptResponse.class).addMappings(mapper -> {
            mapper.map(src -> src.getUser().getId(), AcceptResponse::setUserId);
            mapper.map(src -> src.getUser().getName(), AcceptResponse::setAgenteNome);
            mapper.map(src -> src.getVessel().getId(), AcceptResponse::setVesselId);
            mapper.map(src -> src.getVessel().getNome(), AcceptResponse::setNome);
            // Outros campos como calado_entrada, calado_saida, calado_max, ponte_mfold,
            // mfold_quilha já batem o nome

            // Explicitamente mapeia a lista de berços para DTOs para evitar circularidade
            mapper.using(ctx -> {
                List<br.com.laps.aceite.core.models.Berco> source = (List<br.com.laps.aceite.core.models.Berco>) ctx
                        .getSource();
                if (source == null)
                    return null;
                return source.stream()
                        .map(b -> modelMapper.map(b, br.com.laps.aceite.api.bercos.dtos.BercoResponse.class))
                        .collect(java.util.stream.Collectors.toList());
            }).map(Accept::getBercos, AcceptResponse::setBercos);
        });
    }

    @Override
    public AcceptResponse toAcceptResponse(Accept accept) {
        return modelMapper.map(accept, AcceptResponse.class);
    }

    @Override
    public Accept toAccept(@Valid AcceptRequest acceptRequest) {
        return modelMapper.map(acceptRequest, Accept.class);
    }

}
