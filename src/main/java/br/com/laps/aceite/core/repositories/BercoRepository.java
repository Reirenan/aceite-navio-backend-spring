package br.com.laps.aceite.core.repositories;

import br.com.laps.aceite.core.models.Berco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BercoRepository extends JpaRepository<Berco, Long> {

    //    boolean existsByDwtAndExistsByLoa(Float dwt, Float loa);
    boolean existsByNome(String nome);
    boolean existsByNomeAndIdNot(String nome, Long id);

    @Query("SELECT COUNT(u) FROM Berco u")
    long countAllBercos();

//    @Query("select b from berco b where b.nome >= 100 and b.nome <= 102")
//    List<Berco> findAllBetween();

}
