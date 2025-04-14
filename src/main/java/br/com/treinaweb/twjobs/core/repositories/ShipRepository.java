package br.com.treinaweb.twjobs.core.repositories;

import br.com.treinaweb.twjobs.core.models.Ship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShipRepository extends JpaRepository<Ship, Long> {

    @Override
    Optional<Ship> findById(Long aLong);





}
