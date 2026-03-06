package br.com.laps.aceite.api.audit.mappers;

import br.com.laps.aceite.api.audit.dtos.AuditLogResponse;
import br.com.laps.aceite.core.models.AuditLog;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuditLogMapper {

    private final ObjectMapper objectMapper;

    public AuditLogResponse toResponse(AuditLog log) {
        if (log == null)
            return null;

        AuditLogResponse response = new AuditLogResponse();
        response.setId(log.getId());
        response.setUserEmail(log.getUserEmail());
        response.setIpAddress(log.getIpAddress());
        response.setEntityName(log.getEntityName());
        response.setEntityId(log.getEntityId());
        response.setOperationType(log.getOperationType());
        response.setEndpoint(log.getEndpoint());
        response.setHttpMethod(log.getHttpMethod());
        response.setUserAgent(log.getUserAgent());
        response.setTimestamp(log.getTimestamp());

        try {
            if (log.getOldData() != null) {
                response.setOldData(sanitize(objectMapper.readTree(log.getOldData())));
            }
            if (log.getNewData() != null) {
                response.setNewData(sanitize(objectMapper.readTree(log.getNewData())));
            }
        } catch (Exception e) {
            // Fallback to raw string if JSON parsing fails
            response.setOldData(log.getOldData());
            response.setNewData(log.getNewData());
        }
        return response;
    }

    private JsonNode sanitize(JsonNode node) {
        if (node == null || node.isNull()) {
            return node;
        }

        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;

            // Remove password from any object
            objectNode.remove("password");

            // Identify and strictly sanitize user objects
            if (objectNode.has("email")
                    && (objectNode.has("password") || objectNode.has("role") || objectNode.has("sendEmail"))) {
                sanitizeUser(objectNode);
                return objectNode;
            }

            // Recurse into fields
            List<String> fieldNames = new ArrayList<>();
            objectNode.fieldNames().forEachRemaining(fieldNames::add);

            for (String fieldName : fieldNames) {
                JsonNode child = objectNode.get(fieldName);
                if (child != null) {
                    if (fieldName.equalsIgnoreCase("user") && child.isObject()) {
                        sanitizeUser((ObjectNode) child);
                    } else {
                        sanitize(child);
                    }
                }
            }
        } else if (node.isArray()) {
            for (JsonNode item : node) {
                sanitize(item);
            }
        }

        return node;
    }

    private void sanitizeUser(ObjectNode userNode) {
        JsonNode nameNode = userNode.get("name");
        if (nameNode == null)
            nameNode = userNode.get("nome");
        JsonNode emailNode = userNode.get("email");

        userNode.removeAll();
        if (nameNode != null)
            userNode.set("name", nameNode);
        if (emailNode != null)
            userNode.set("email", emailNode);
    }
}
