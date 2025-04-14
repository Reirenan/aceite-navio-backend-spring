//@Transactional : se der algum erro toda a operação é cancelada

package br.com.treinaweb.twjobs.core.services;


import br.com.treinaweb.twjobs.core.exceptions.NegocioException;
import br.com.treinaweb.twjobs.core.models.Navio;
import br.com.treinaweb.twjobs.core.repositories.NavioRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

//Gerenciar o CRUD simples
@AllArgsConstructor
@Service
public class CadastroNavioService {

    NavioRepository navioRepository;



    @Transactional
    public Navio salvar(Navio navio) {

        boolean imo_existe = navioRepository.findByImo(navio.getImo()).filter(n ->!n.equals(navio)).isPresent();

        if(imo_existe){
            throw new NegocioException("Já existe navio cadastrado com esse IMO.");
        }

       // if(navio.getImo() != 34) {
        //    throw new NegocioException("O IMO não segue o padrão");
      //  }

        return navioRepository.save(navio);
    }



}
