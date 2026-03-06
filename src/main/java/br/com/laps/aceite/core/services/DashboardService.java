package br.com.laps.aceite.core.services;

import br.com.laps.aceite.api.dashboard.dtos.DashboardSummaryDTO;
import br.com.laps.aceite.core.enums.AceiteStatus;
import br.com.laps.aceite.core.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final VesselRepository vesselRepository;
    private final UserRepository userRepository;
    private final BercoRepository bercoRepository;
    private final AcceptRepository acceptRepository;
    private final VettingRepository vettingRepository;

    @Transactional(readOnly = true)
    public DashboardSummaryDTO getSummary() {
        return DashboardSummaryDTO.builder()
                .totalVessels(vesselRepository.count())
                .totalUsers(userRepository.count())
                .totalBerths(bercoRepository.count())
                .totalAccepts(acceptRepository.count())
                .totalRedList(vettingRepository.count())
                .acceptedNavios(countByStatus(AceiteStatus.ACEITO))
                .rejectedNavios(countByStatus(AceiteStatus.NEGADO))
                .pendingNavios(countByStatus(AceiteStatus.EM_PROCESSAMENTO))
                .build();
    }

    private long countByStatus(AceiteStatus status) {
        return acceptRepository.countByStatus(status);
    }
}
