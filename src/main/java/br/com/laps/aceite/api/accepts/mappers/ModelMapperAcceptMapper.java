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
        AcceptResponse response = modelMapper.map(accept, AcceptResponse.class);

        if (accept.getVessel() != null) {
            response.setVesselId(accept.getVessel().getId());
            // Dados constantes do Navio
            response.setNome(accept.getVessel().getNome());
            response.setImo(accept.getVessel().getImo());
            response.setMmsi(accept.getVessel().getMmsi());
            response.setLoa(accept.getVessel().getLoa());
            response.setBoca(accept.getVessel().getBoca());
            response.setDwt(accept.getVessel().getDwt());
            response.setPontal(accept.getVessel().getPontal());
            response.setCalado_max(accept.getVessel().getCalado_max());
            response.setCategoria(accept.getVessel().getCategoria());
            response.setFlag(accept.getVessel().getFlag());
        }

        if (accept.getUser() != null) {
            response.setUserId(accept.getUser().getId());
            response.setAgenteNome(accept.getUser().getName());
        }

        // Dados específicos do Aceite (Previsão)
        response.setStatus(accept.getStatus());
        response.setCaladoEntrada(accept.getCaladoEntrada());
        response.setCaladoSaida(accept.getCaladoSaida());
        response.setPonte_mfold(accept.getPonteMfold());
        response.setMfold_quilha(accept.getMfoldQuilha());

        return response;
    }

    @Override
    public Accept toAccept(@Valid AcceptRequest acceptRequest) {
        Accept accept = modelMapper.map(acceptRequest, Accept.class);

        // Mapeamento manual para campos com divergência de nomenclatura snake_case vs
        // camelCase
        if (acceptRequest.getPonte_mfold() != null) {
            accept.setPonteMfold(acceptRequest.getPonte_mfold());
        }
        if (acceptRequest.getMfold_quilha() != null) {
            accept.setMfoldQuilha(acceptRequest.getMfold_quilha());
        }

        return accept;
    }

}
