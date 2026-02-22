package br.com.laps.aceite.core.services.navio;

import br.com.laps.aceite.core.exceptions.NegocioException;
import br.com.laps.aceite.core.models.Vessel;
import br.com.laps.aceite.core.repositories.VesselRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CadastroVesselService {

    VesselRepository vesselRepository;



    @Transactional
    public Vessel salvar(Vessel vessel) {

        boolean imo_existe = vesselRepository.findByImo(vessel.getImo()).filter(n ->!n.equals(vessel)).isPresent();

        if(imo_existe){
            throw new NegocioException("Já existe navio cadastrado com esse IMO.");
        }

        return vesselRepository.save(vessel);
    }


}
