package br.com.laps.aceite.core.repositories;

import br.com.laps.aceite.core.models.Vetting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VettingRepository extends JpaRepository<Vetting, Long> {

    Page<Vetting> findAllByUserId(Long userId, Pageable pageable);

    boolean existsByImo(String imo);

    @Query("SELECT COUNT(v) FROM Vetting v")
    long countAllVettings();
}