package br.com.laps.aceite.core.repositories;

import br.com.laps.aceite.core.models.Accept;
import br.com.laps.aceite.core.enums.AceiteStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AcceptRepository extends JpaRepository<Accept, Long> {

    // Último Accept pela dataHoraAccept
    Accept findFirstByOrderByDataHoraAcceptDesc();

    // Último Accept geral (pelo ID)
    Accept findTop1ByOrderByIdDesc();

    // Último Accept por IMO (IMO é String)
    Accept findTop1ByImoOrderByIdDesc(String imo);

    // Buscar por IMO
    List<Accept> findAllByImo(String imo);

    // Paginação por usuário
    Page<Accept> findAllByUserId(Long userId, Pageable pageable);

    // Buscar por id e usuário (segurança)
    Optional<Accept> findByIdAndUserId(Long id, Long userId);

    // Contagem total
    @Query("SELECT COUNT(a) FROM Accept a")
    Long countAllAccepts();

    // Contagem agrupada por status
    @Query("SELECT a.status, COUNT(a) FROM Accept a GROUP BY a.status")
    List<Object[]> countByStatus();

    // Filtro por status
    Page<Accept> findAllByStatus(AceiteStatus status, Pageable pageable);

    // Intervalo de data
    List<Accept> findAllByDataHoraAcceptBetween(
            java.time.LocalDateTime start,
            java.time.LocalDateTime end
    );
}