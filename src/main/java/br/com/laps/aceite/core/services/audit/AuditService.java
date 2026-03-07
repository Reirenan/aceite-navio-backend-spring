package br.com.laps.aceite.core.services.audit;

import br.com.laps.aceite.core.enums.OperationType;
import br.com.laps.aceite.core.models.AuditLog;
import br.com.laps.aceite.core.repositories.AuditLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository repository;
    private final ObjectMapper objectMapper;

    /**
     * Registra uma entrada de auditoria de forma assíncrona.
     * Nunca lança exceção para não impactar a transação principal.
     *
     * @param operation  tipo da operação (CREATE, UPDATE, DELETE)
     * @param entity     nome da entidade
     * @param entityId   id da entidade
     * @param oldObject  estado anterior (já serializado em String JSON)
     * @param newObject  novo estado (já serializado em String JSON)
     * @param ipAddress  IP do cliente
     * @param endpoint   URI do endpoint
     * @param httpMethod método HTTP
     * @param userAgent  User-Agent do cliente
     * @param userEmail  email do usuário autenticado
     */
    @Async("auditExecutor")
    public void log(OperationType operation,
            String entity,
            Long entityId,
            String oldObject,
            String newObject,
            String ipAddress,
            String endpoint,
            String httpMethod,
            String userAgent,
            String userEmail) {
        try {
            AuditLog auditLog = new AuditLog();
            auditLog.setTimestamp(Instant.now());
            auditLog.setUserEmail(userEmail != null ? userEmail : "SYSTEM");
            auditLog.setIpAddress(ipAddress);
            auditLog.setEndpoint(endpoint);
            auditLog.setHttpMethod(httpMethod);
            auditLog.setUserAgent(userAgent);
            auditLog.setEntityName(entity);
            auditLog.setEntityId(entityId);
            auditLog.setOperationType(operation);
            auditLog.setOldData(oldObject);
            auditLog.setNewData(newObject);

            repository.save(auditLog);

        } catch (Exception e) {
            // Auditoria nunca deve derrubar o request principal
            log.error("Erro ao registrar auditoria para entidade '{}' id={}: {}", entity, entityId, e.getMessage(), e);
        }
    }
}
