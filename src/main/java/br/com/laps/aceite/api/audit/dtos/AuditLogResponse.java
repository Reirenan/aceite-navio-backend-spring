package br.com.laps.aceite.api.audit.dtos;

import br.com.laps.aceite.core.enums.OperationType;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class AuditLogResponse {
    private Long id;
    private String userEmail;
    private String ipAddress;
    private String entityName;
    private Long entityId;
    private OperationType operationType;
    private String endpoint;
    private String httpMethod;
    private String userAgent;
    private Instant timestamp;
    private Object oldData;
    private Object newData;
}
