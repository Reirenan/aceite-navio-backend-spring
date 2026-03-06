package br.com.laps.aceite.core.services.navio;

import br.com.laps.aceite.core.exceptions.NegocioException;
import br.com.laps.aceite.core.models.Vessel;
import br.com.laps.aceite.core.repositories.VesselRepository;
import br.com.laps.aceite.core.services.audit.Auditable;
import br.com.laps.aceite.core.exceptions.VesselNotFoundException;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CadastroVesselService {

    VesselRepository vesselRepository;

    @Auditable(entity = "Vessel", clazz = Vessel.class)
    @Transactional
    public Vessel salvar(Vessel vessel) {

        boolean imo_existe = vesselRepository.findByImo(vessel.getImo()).filter(n -> !n.equals(vessel)).isPresent();

        if (imo_existe) {
            throw new NegocioException("Já existe navio cadastrado com esse IMO.");
        }

        return vesselRepository.save(vessel);
    }

    @Auditable(entity = "Vessel", clazz = Vessel.class)
    @Transactional
    public void excluir(Long id) {
        Vessel vessel = buscarOuFalhar(id);
        vesselRepository.delete(vessel);
    }

    public Vessel buscarOuFalhar(Long id) {
        return vesselRepository.findById(id)
                .orElseThrow(VesselNotFoundException::new);
    }

}
