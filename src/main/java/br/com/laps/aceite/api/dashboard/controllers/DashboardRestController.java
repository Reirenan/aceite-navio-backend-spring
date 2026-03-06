package br.com.laps.aceite.api.dashboard.controllers;

import br.com.laps.aceite.api.dashboard.dtos.DashboardSummaryDTO;
import br.com.laps.aceite.core.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardRestController {

        private final DashboardService dashboardService;

    @GetMapping("/summary")
    public DashboardSummaryDTO getSummary() {
        System.out.println("DEBUG: Dashboard request received");
        return dashboardService.getSummary();
    }
}
