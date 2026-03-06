package br.com.treinaweb.twjobs;

import br.com.laps.aceite.core.enums.OperationType;
import br.com.laps.aceite.core.models.Vessel;
import br.com.laps.aceite.core.repositories.AuditLogRepository;
import br.com.laps.aceite.core.repositories.VesselRepository;
import br.com.laps.aceite.core.repositories.UserRepository;
import br.com.laps.aceite.core.services.navio.CadastroVesselService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuditSystemIntegrationTest {

    @Autowired
    private CadastroVesselService cadastroVesselService;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private VesselRepository vesselRepository;

    @Autowired
    private UserRepository userRepository;

    private br.com.laps.aceite.core.models.User createTestUser() {
        var user = new br.com.laps.aceite.core.models.User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(br.com.laps.aceite.core.enums.Role.ADMINISTRADOR);
        return userRepository.save(user);
    }

    @Test
    @Transactional
    void shouldLogCreateVessel() {
        var user = createTestUser();
        Vessel vessel = new Vessel();
        vessel.setNome("Test Vessel");
        vessel.setImo("1234567");
        vessel.setUser(user);
        vessel.setCategoria("GENERAL");
        vessel.setLoa(100.0);
        vessel.setDwt(5000.0);
        vessel.setCalado_max(10.0);

        Vessel savedVessel = cadastroVesselService.salvar(vessel);

        var logs = auditLogRepository.findByEntityNameAndEntityId("Vessel", savedVessel.getId());
        assertThat(logs).isNotEmpty();
        assertThat(logs.get(0).getOperationType()).isEqualTo(OperationType.CREATE);
        assertThat(logs.get(0).getNewData()).contains("Test Vessel");
    }

    @Test
    @Transactional
    void shouldLogDeleteVessel() {
        var user = createTestUser();
        Vessel vessel = new Vessel();
        vessel.setNome("Vessel to Delete");
        vessel.setImo("7654321");
        vessel.setUser(user);
        vessel.setCategoria("GENERAL");
        vessel.setLoa(100.0);
        vessel.setDwt(5000.0);
        vessel.setCalado_max(10.0);

        Vessel savedVessel = cadastroVesselService.salvar(vessel);

        cadastroVesselService.excluir(savedVessel.getId());

        var logs = auditLogRepository.findByEntityNameAndEntityId("Vessel", savedVessel.getId());
        // Find specifically the DELETE log
        var deleteLog = logs.stream()
                .filter(log -> log.getOperationType() == OperationType.DELETE)
                .findFirst();

        assertThat(deleteLog).isPresent();
        assertThat(deleteLog.get().getOldData()).contains("Vessel to Delete");
    }
}
