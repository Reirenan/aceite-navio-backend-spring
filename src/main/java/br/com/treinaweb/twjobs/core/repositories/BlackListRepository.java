package br.com.treinaweb.twjobs.core.repositories;



import br.com.treinaweb.twjobs.core.models.BlackList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {

    Page<BlackList> findAllByUserId(Pageable pageable, Long userId);

    boolean existsByImo(Long imo);

    @Query("SELECT COUNT(u) FROM BlackList u")
    long countAllBlackLists();


}
