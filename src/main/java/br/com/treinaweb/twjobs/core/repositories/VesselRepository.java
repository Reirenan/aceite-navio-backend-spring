package br.com.treinaweb.twjobs.core.repositories;

import br.com.treinaweb.twjobs.api.vessels.dtos.VesselResponse;
import br.com.treinaweb.twjobs.core.models.User;
import br.com.treinaweb.twjobs.core.models.Vessel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface VesselRepository extends JpaRepository<Vessel, Long> {

    List<Vessel> findByNome(String nome);
    List<Vessel> findByNomeContaining(String nome);
    Optional<Vessel> findByImo(Long imo);
    Optional<Vessel> findByImoAndUserId(Long imo, Long userId);

//    Vessel findByImo(Long imo);
    boolean existsByImo(Long imo);
    boolean existsByImoAndIdNot(Long imo, Long id);

    Page<Vessel> findAllByUser(Optional<User> user, Pageable pageable);

    Page<Vessel> findAllByUserId(Pageable pageable, Long userId);
    Page<Vessel> findByUserId(Pageable pageable, Long userId);

    @Query("SELECT COUNT(u) FROM Vessel u")
    Long countAllVessels();

    Optional<Vessel> findTopByImoOrderByIdDesc(Long imo);

//    Vessel findAllByUser(User user);

//    List<Vessel> findAll();
}
