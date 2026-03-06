package br.com.laps.aceite.api.dashboard.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardSummaryDTO {
    private long totalVessels;
    private long totalUsers;
    private long totalBerths;
    private long totalAccepts;
    private long totalRedList;

    // Status counts
    private long acceptedNavios;
    private long rejectedNavios;
    private long pendingNavios;
}
