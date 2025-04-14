package br.com.treinaweb.twjobs.core.repositories;

import br.com.treinaweb.twjobs.core.models.Accept;
import br.com.treinaweb.twjobs.core.models.Aceite;
import br.com.treinaweb.twjobs.core.models.User;
import br.com.treinaweb.twjobs.core.models.Vessel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AcceptRepository extends JpaRepository<Accept, Long> {





















      Accept findFirstByOrderByDataAcceptDesc();

//    Accept findFirstByOrderByPublicationDateDesc(Long imo);
//    Accept findByImoOrderByImoDesc(Long imo);
      @Query("SELECT p FROM Accept p ORDER BY p.id DESC LIMIT 1")
      Accept FindLastAccept();


//      Accept findTopByOrderByImoDesc(@Param("Imo")Long imo);

        Accept findByImoOrderByIdDesc(Long imo);


      Accept findTop1ByImoOrderByIdDesc(@Param("imo") Long imo);

      Page<Accept> findAllByUser(Optional<User> user, Pageable pageable);

//    SELECT a FROM ( SELECT b, ROW_NUMBER() OVER (ORDER BY id DESC) AS rn FROM accept ) AS subquery a WHERE a.rn = 1 AND a.imo=?1;
//    @Query("SELECT a from( SELECT b, ROW_ )")
//    @Query("SELECT a FROM Accept a WHERE a.codigo = '1' AND a.imo=?1" )
//    Accept findAcceptByLastImo(Long imo);

//      Funciona para PostgreeSQL e MySQL
//      @Query("SELECT e FROM Accept e WHERE CAST(e.imo AS string) LIKE %:paramImo%")
//      List<Accept> findAllByImoContaining(String paramImo);







      Page<Accept> findAllByUserId(Pageable pageable, Long userId);

      Optional<Accept> findByIdAndUserId(Long id, Long userId);


      @Query("SELECT COUNT(u) FROM Accept u")
      Long countAllAccepts();


      @Query("SELECT a.status, COUNT(a) FROM Accept a GROUP BY a.status")
      List<Object[]> countByStatus();

//      List<Accept> findAllByImo(Long imo);
//    Criar o find status by id.
//      PEga o id do vessel que "findByImoAndFindFirstByOrderByPublicationDateDesc" retorna



}
