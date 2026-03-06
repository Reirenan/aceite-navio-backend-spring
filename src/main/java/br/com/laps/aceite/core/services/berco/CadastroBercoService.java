package br.com.laps.aceite.core.services.berco;

import br.com.laps.aceite.core.exceptions.BercoNotFoundException;
import br.com.laps.aceite.core.models.Berco;
import br.com.laps.aceite.core.repositories.BercoRepository;
import br.com.laps.aceite.core.services.audit.Auditable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CadastroBercoService {

    private final BercoRepository bercoRepository;

    @Auditable(entity = "Berco", clazz = Berco.class)
    @Transactional
    public Berco salvar(Berco berco) {
        return bercoRepository.save(berco);
    }

    @Auditable(entity = "Berco", clazz = Berco.class)
    @Transactional
    public void excluir(Long id) {
        Berco berco = buscarOuFalhar(id);
        bercoRepository.delete(berco);
    }

    public Berco buscarOuFalhar(Long id) {
        return bercoRepository.findById(id)
                .orElseThrow(BercoNotFoundException::new);
    }
}
