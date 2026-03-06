package br.com.laps.aceite.core.services.audit;

import br.com.laps.aceite.core.enums.OperationType;
import br.com.laps.aceite.core.models.AuditLog;
import br.com.laps.aceite.core.repositories.AuditLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository repository;
    private final ObjectMapper objectMapper;

    public void log(OperationType operation,
            String entity,
            Long entityId,
            Object oldObject,
            Object newObject,
            HttpServletRequest request) {

        try {
            AuditLog log = new AuditLog();

            log.setTimestamp(Instant.now());

            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                log.setUserEmail(auth.getName());
            } else {
                log.setUserEmail("SYSTEM");
            }

            if (request != null) {
                log.setIpAddress(getClientIp(request));
                log.setEndpoint(request.getRequestURI());
                log.setHttpMethod(request.getMethod());
                log.setUserAgent(request.getHeader("User-Agent"));
            }

            log.setEntityName(entity);
            log.setEntityId(entityId);
            log.setOperationType(operation);

            if (oldObject != null) {
                log.setOldData(objectMapper.writeValueAsString(oldObject));
            }

            if (newObject != null) {
                log.setNewData(objectMapper.writeValueAsString(newObject));
            }

            repository.save(log);

        } catch (Exception e) {
            // In a real scenario, we might want to log this error but not break the main
            // transaction
            // For now, following the user's design:
            throw new RuntimeException("Erro ao registrar auditoria", e);
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        } else {
            // X-Forwarded-For can contain multiple IPs, the first one is the client
            int index = ip.indexOf(',');
            if (index != -1) {
                ip = ip.substring(0, index);
            }
        }
        return ip;
    }
}
