package br.com.laps.aceite.api.audit.controllers;

import br.com.laps.aceite.api.audit.dtos.AuditLogResponse;
import br.com.laps.aceite.api.audit.mappers.AuditLogMapper;
import br.com.laps.aceite.core.repositories.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AuditRestController {

    private final AuditLogRepository repository;
    private final AuditLogMapper mapper;

    @GetMapping("/api/audit/history/record/{entity}/{id}")
    public List<AuditLogResponse> getHistoryByRecord(@PathVariable String entity, @PathVariable Long id) {
        return repository.findByEntityNameAndEntityId(entity, id).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/audit/history/user/{email}")
    public List<AuditLogResponse> getHistoryByUser(@PathVariable String email) {
        return repository.findByUserEmail(email).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/audit/history/entity/{entity}")
    public List<AuditLogResponse> getHistoryByEntity(@PathVariable String entity) {
        return repository.findByEntityName(entity).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/audit/history")
    public List<AuditLogResponse> getAllHistory() {
        return repository.findAllByOrderByTimestampDesc().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}
