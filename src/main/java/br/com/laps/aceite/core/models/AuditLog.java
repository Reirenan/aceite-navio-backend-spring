package br.com.laps.aceite.core.models;

import br.com.laps.aceite.core.enums.OperationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "audit_log")
@Getter
@Setter
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;

    private String ipAddress;

    private String entityName;

    private Long entityId;

    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    private String endpoint;

    private String httpMethod;

    @Column(length = 500)
    private String userAgent;

    private Instant timestamp;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String oldData;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String newData;
}
