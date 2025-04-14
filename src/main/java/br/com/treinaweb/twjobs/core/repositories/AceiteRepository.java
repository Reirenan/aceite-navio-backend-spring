package br.com.treinaweb.twjobs.core.repositories;

import br.com.treinaweb.twjobs.core.models.Aceite;
import br.com.treinaweb.twjobs.core.models.Navio;
import br.com.treinaweb.twjobs.core.models.Vessel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AceiteRepository extends JpaRepository<Aceite, Long>{

  //  boolean existsByCompanyEmailAndId(String companyEmail, Long id);
      boolean findByImo(Long imo);
//    Optional<Vessel> findByImo(Long imo);
//    Long findVanessaByGorda(Long vane);

}
