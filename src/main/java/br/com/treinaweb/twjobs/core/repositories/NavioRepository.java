package br.com.treinaweb.twjobs.core.repositories;


import br.com.treinaweb.twjobs.core.models.Navio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NavioRepository extends JpaRepository<Navio, Long> {

   List<Navio> findByNome(String nome);
   List<Navio> findByNomeContaining(String nome);
   Optional<Navio> findByImo(Long imo);




}
