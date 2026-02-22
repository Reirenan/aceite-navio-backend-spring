package br.com.treinaweb.twjobs.core.services;

import br.com.treinaweb.twjobs.core.exceptions.NegocioException;
import br.com.treinaweb.twjobs.core.models.Vessel;
import br.com.treinaweb.twjobs.core.repositories.VesselRepository;
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
