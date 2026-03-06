package br.com.laps.aceite.core.services.vetting;

import br.com.laps.aceite.core.exceptions.VettingNotFoundException;
import br.com.laps.aceite.core.models.Vetting;
import br.com.laps.aceite.core.repositories.VettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CadastroVettingService {

    private final VettingRepository vettingRepository;

    @Transactional
    public Vetting salvar(Vetting vetting) {
        return vettingRepository.save(vetting);
    }

    @Transactional
    public void excluir(Long id) {
        var vetting = buscarOuFalhar(id);
        vettingRepository.delete(vetting);
    }

    public Vetting buscarOuFalhar(Long id) {
        return vettingRepository.findById(id)
                .orElseThrow(() -> new VettingNotFoundException());
    }
}
