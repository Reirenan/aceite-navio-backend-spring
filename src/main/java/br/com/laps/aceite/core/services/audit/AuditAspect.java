package br.com.laps.aceite.core.services.audit;

import br.com.laps.aceite.core.enums.OperationType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;
    private final EntityManager entityManager;
    private final ObjectMapper objectMapper;

    @Around("@annotation(auditable)")
    public Object audit(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        String entityName = auditable.entity();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        // ── Captura dados do request HTTP antes de qualquer coisa (thread principal)
        // ──
        String ipAddress = null;
        String endpoint = null;
        String httpMethod = null;
        String userAgent = null;
        String userEmail = "SYSTEM";

        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest req = attrs.getRequest();
                ipAddress = getClientIp(req);
                endpoint = req.getRequestURI();
                httpMethod = req.getMethod();
                userAgent = req.getHeader("User-Agent");
            }

            var auth = org.springframework.security.core.context.SecurityContextHolder
                    .getContext().getAuthentication();
            if (auth != null && auth.getName() != null) {
                userEmail = auth.getName();
            }
        } catch (Exception e) {
            log.warn("AuditAspect: não foi possível capturar dados do request: {}", e.getMessage());
        }

        // ── Determina ID e Classe ──
        Long entityId = null;
        Class<?> entityClass = auditable.clazz() != Object.class ? auditable.clazz() : null;

        if (args.length > 0) {
            Object firstArg = args[0];
            if (firstArg instanceof Long) {
                entityId = (Long) firstArg;
            } else if (firstArg != null) {
                entityId = extractId(firstArg);
                if (entityClass == null) {
                    entityClass = firstArg.getClass();
                }
            }
        }

        OperationType operationType = determineOperationType(methodName, entityId);

        // ── Captura estado anterior (UPDATE / DELETE) — dentro da transação ──
        String oldDataJson = null;
        if ((operationType == OperationType.UPDATE || operationType == OperationType.DELETE)
                && entityId != null && entityClass != null) {
            try {
                Object oldEntity = entityManager.find(entityClass, entityId);
                if (oldEntity != null) {
                    // Serializa ANTES de detach para ter acesso ao estado carregado
                    oldDataJson = safeSerialize(oldEntity);
                    entityManager.detach(oldEntity);
                }
            } catch (Exception e) {
                log.warn("AuditAspect: não foi possível capturar oldState de {} id={}: {}",
                        entityName, entityId, e.getMessage());
            }
        }

        // ── Executa o método original ──
        Object result = joinPoint.proceed();

        // ── Captura novo estado (CREATE / UPDATE) ──
        String newDataJson = null;
        if (operationType == OperationType.CREATE || operationType == OperationType.UPDATE) {
            try {
                Object target = unwrapResult(result);
                if (target != null) {
                    newDataJson = safeSerialize(target);
                    if (entityId == null) {
                        entityId = extractId(target);
                    }
                }
            } catch (Exception e) {
                log.warn("AuditAspect: não foi possível capturar newState de {}: {}",
                        entityName, e.getMessage());
            }
        }

        // ── Dispara auditoria assíncrona ──
        auditService.log(
                operationType,
                entityName,
                entityId,
                oldDataJson,
                newDataJson,
                ipAddress,
                endpoint,
                httpMethod,
                userAgent,
                userEmail);

        return result;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    /**
     * Desempacota EntityModel para obter o conteúdo real.
     */
    @SuppressWarnings("rawtypes")
    private Object unwrapResult(Object result) {
        if (result == null)
            return null;
        if (result instanceof EntityModel em) {
            return em.getContent();
        }
        return result;
    }

    /**
     * Serialização segura: tenta via ObjectMapper; se falhar (proxies Hibernate),
     * cai em serialização manual via reflection de campos simples.
     */
    private String safeSerialize(Object obj) {
        if (obj == null)
            return null;
        try {
            // Tenta serialização normal
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            // Fallback: extrai campos simples via reflection
            try {
                Map<String, Object> map = extractSimpleFields(obj);
                return objectMapper.writeValueAsString(map);
            } catch (Exception ex) {
                // Último fallback: apenas o ID
                try {
                    Long id = extractId(obj);
                    return "{\"id\":" + id + "}";
                } catch (Exception ignored) {
                    return "{\"error\":\"nao_serializavel\"}";
                }
            }
        }
    }

    /**
     * Extrai os campos "simples" das entidades (primitivos, String, Enum, Number,
     * LocalDate*, Instant).
     * Ignora relações JPA (para evitar LazyInitializationException).
     */
    private Map<String, Object> extractSimpleFields(Object obj) {
        Map<String, Object> map = new LinkedHashMap<>();
        Class<?> clazz = obj.getClass();
        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    Object value = field.get(obj);
                    if (isSimpleType(value)) {
                        map.put(field.getName(), value);
                    }
                } catch (Exception ignored) {
                    // ignora campos inacessíveis
                }
            }
            clazz = clazz.getSuperclass();
        }
        return map;
    }

    private boolean isSimpleType(Object value) {
        if (value == null)
            return true;
        return value instanceof Number
                || value instanceof String
                || value instanceof Boolean
                || value instanceof Enum
                || value.getClass().getName().startsWith("java.time.");
    }

    private OperationType determineOperationType(String methodName, Long id) {
        if (methodName.startsWith("excluir") || methodName.startsWith("delete")
                || methodName.startsWith("remover")) {
            return OperationType.DELETE;
        }
        if (id != null) {
            return OperationType.UPDATE;
        }
        return OperationType.CREATE;
    }

    private Long extractId(Object obj) {
        if (obj == null)
            return null;
        if (obj instanceof Long l)
            return l;
        try {
            Field idField = obj.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            Object val = idField.get(obj);
            if (val instanceof Long l)
                return l;
        } catch (Exception ignored) {
        }
        return null;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        } else {
            int index = ip.indexOf(',');
            if (index != -1)
                ip = ip.substring(0, index).trim();
        }
        return ip;
    }
}
