package br.com.treinaweb.twjobs.core.service;

import br.com.treinaweb.twjobs.core.repositories.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final VesselRepository vesselRepository;
    private final AcceptRepository acceptRepository;
    private final UserRepository userRepository;
    private final BlackListRepository blackListRepository;
    private final BercoRepository bercosRepository;

    public StatisticsService(VesselRepository vesselRepository,
                             AcceptRepository acceptRepository,
                             UserRepository userRepository,
                             BlackListRepository blackListRepository,
                             BercoRepository bercosRepository) {
        this.vesselRepository = vesselRepository;
        this.acceptRepository = acceptRepository;
        this.userRepository = userRepository;
        this.blackListRepository = blackListRepository;
        this.bercosRepository = bercosRepository;
    }

    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // Contagem dos status
        List<Object[]> statusCounts = acceptRepository.countByStatus();
        Map<String, Long> statusMap = statusCounts.stream()
                .collect(Collectors.toMap(
                        result -> (String) result[0],
                        result -> (Long) result[1]
                ));

        // Contagem geral de entidades
        statistics.put("statusCounts", statusMap);
        statistics.put("totalVessels", vesselRepository.countAllVessels());
        statistics.put("totalAccepts", acceptRepository.countAllAccepts());
        statistics.put("totalUsers", userRepository.countAllUsers());
        statistics.put("totalBlackLists", blackListRepository.countAllBlackLists());
        statistics.put("totalBercos", bercosRepository.countAllBercos());

        return statistics;
    }
}
