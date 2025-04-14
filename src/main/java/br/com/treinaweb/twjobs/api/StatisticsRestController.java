package br.com.treinaweb.twjobs.api;

import br.com.treinaweb.twjobs.core.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/statistics")
public class StatisticsRestController {

    private final StatisticsService statisticsService;



    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllStatistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }


}
