package br.com.laps.aceite.core.services.audit;

import br.com.laps.aceite.core.enums.OperationType;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;
    private final EntityManager entityManager;

    @Around("@annotation(auditable)")
    public Object audit(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        String entityName = auditable.entity();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        Object oldState = null;
        Object newState = null;
        Long entityId = null;

        // Try to determine ID and Class
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

        // Pre-execution (Capture old state for UPDATE and DELETE)
        if ((operationType == OperationType.UPDATE || operationType == OperationType.DELETE)
                && entityId != null && entityClass != null) {
            try {
                oldState = entityManager.find(entityClass, entityId);
                // Evict from persistence context to ensure we don't get the updated one later
                if (oldState != null) {
                    entityManager.detach(oldState);
                }
            } catch (Exception e) {
                log.warn("Could not fetch old state for {} with id {}", entityName, entityId);
            }
        }

        // Execute original method
        Object result = joinPoint.proceed();

        // Post-execution (Capture new state for CREATE and UPDATE)
        if (operationType == OperationType.CREATE || operationType == OperationType.UPDATE) {
            newState = result;
            if (entityId == null) {
                entityId = extractId(newState);
            }
        }

        // Send to AuditService
        HttpServletRequest request = null;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            request = attributes.getRequest();
        }

        auditService.log(operationType, entityName, entityId, oldState, newState, request);

        return result;
    }

    private OperationType determineOperationType(String methodName, Long id) {
        if (methodName.startsWith("excluir") || methodName.startsWith("delete") || methodName.startsWith("remover")) {
            return OperationType.DELETE;
        }

        // If it's a save/update method, check if ID exists
        if (id != null) {
            return OperationType.UPDATE;
        }

        return OperationType.CREATE;
    }

    private Long extractId(Object obj) {
        if (obj == null)
            return null;
        if (obj instanceof Long)
            return (Long) obj;

        try {
            Field idField = obj.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            return (Long) idField.get(obj);
        } catch (Exception e) {
            return null;
        }
    }
}
