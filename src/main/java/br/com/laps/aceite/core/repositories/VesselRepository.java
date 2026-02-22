package br.com.laps.aceite.core.repositories;

import br.com.laps.aceite.core.models.User;
import br.com.laps.aceite.core.models.Vessel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VesselRepository extends JpaRepository<Vessel, Long> {

    List<Vessel> findByNome(String nome);
    List<Vessel> findByNomeContaining(String nome);
    Optional<Vessel> findByImo(String imo);
    Optional<Vessel> findByImoAndUserId(String imo, Long userId);

    //    Vessel findByImo(String imo);
    boolean existsByImo(String imo);
    boolean existsByImoAndIdNot(String imo, Long id);

    Page<Vessel> findAllByUser(Optional<User> user, Pageable pageable);

    Page<Vessel> findAllByUserId(Pageable pageable, Long userId);
    Page<Vessel> findByUserId(Pageable pageable, Long userId);

    @Query("SELECT COUNT(u) FROM Vessel u")
    Long countAllVessels();

    Optional<Vessel> findTopByImoOrderByIdDesc(String imo);

}
