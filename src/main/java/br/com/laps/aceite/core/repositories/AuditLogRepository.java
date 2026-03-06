package br.com.laps.aceite.core.repositories;

import br.com.laps.aceite.core.models.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByEntityNameAndEntityId(String entityName, Long entityId);

    List<AuditLog> findByUserEmail(String userEmail);

    List<AuditLog> findByEntityName(String entityName);

    List<AuditLog> findAllByOrderByTimestampDesc();
}
