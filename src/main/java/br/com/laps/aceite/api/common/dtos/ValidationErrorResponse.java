package br.com.laps.aceite.api.common.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorResponse {

    private String message;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private Map<String, List<String>> errors;
    
}
