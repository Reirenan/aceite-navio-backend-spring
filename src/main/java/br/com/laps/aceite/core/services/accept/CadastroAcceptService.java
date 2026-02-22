package br.com.laps.aceite.core.services.accept;


import br.com.laps.aceite.api.accepts.assemblers.AcceptAssembler;
import br.com.laps.aceite.api.accepts.dtos.AcceptRequest;
import br.com.laps.aceite.api.accepts.dtos.AcceptResponse;
import br.com.laps.aceite.api.accepts.mappers.AcceptMapper;
import br.com.laps.aceite.api.file.FileManagerController;
import br.com.laps.aceite.core.enums.AceiteStatus;
import br.com.laps.aceite.core.exceptions.NegocioException;
import br.com.laps.aceite.core.exceptions.VesselNotFoundException;
import br.com.laps.aceite.core.models.*;
import br.com.laps.aceite.core.repositories.*;
import br.com.laps.aceite.core.services.auth.SecurityService;
import br.com.laps.aceite.core.services.email.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@AllArgsConstructor
@Service
public class CadastroAcceptService {

    private final AcceptMapper acceptMapper;
    private final AcceptAssembler acceptAssembler;
    private final AcceptRepository acceptRepository;
    private final SecurityService securityService;
    private final PagedResourcesAssembler<AcceptResponse> pagedResourcesAssembler;

    private final VesselRepository vesselRepository;
    private final BercoRepository bercoRepository;
    private final VettingRepository vettingRepository;

    private final FileManagerController fileManagerController;


    private final UserRepository userRepository;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private EmailService emailService;


    @Transactional
    public EntityModel<AcceptResponse> salvar(String acceptRequestForm, MultipartFile foto, String destinatario)
            throws JsonProcessingException {

        String msg = null;

        // 1) Parse do request
        AcceptRequest acceptRequest = mapper.readValue(acceptRequestForm, AcceptRequest.class);

        // 2) Usuário logado
        User user = securityService.getCurrentUser();

        // 3) Mapeia DTO -> Entity
        Accept accept = acceptMapper.toAccept(acceptRequest);
        accept.setUser(user);

        // 4) Upload (se tiver foto) + valida extensão
        if (foto != null && !foto.isEmpty()) {
            String original = foto.getOriginalFilename();
            String ext = null;

            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf('.') + 1).toLowerCase();
            }

            List<String> allowed = List.of("txt", "zip", "pdf");
            if (ext == null || !allowed.contains(ext)) {
                throw new NegocioException("Extensão inválida: " + ext);
            }

            accept.setPath(original);
            fileManagerController.uploadFile(foto);
        }

        // 5) Destinatário dinâmico
        User destinatarioUser = userRepository.findBySendEmail(Boolean.TRUE)
                .orElseGet(() -> {
                    User fallback = new User();
                    fallback.setEmail("suporte@sistema.com");
                    return fallback;
                });

        destinatario = destinatarioUser.getEmail();

        // 6) Vessel por IMO
        Vessel vessel = vesselRepository.findByImo(accept.getImo())
                .orElseThrow(VesselNotFoundException::new);

        accept.setVessel(vessel);

        // 7) Data/hora (novo campo)
        accept.setDataHoraAccept(java.time.LocalDateTime.now());

        // 8) Checa blacklist
        boolean blackListed = vettingRepository.existsByImo(accept.getImo());

        // 9) Berços compatíveis
        List<Berco> bercos = bercoRepository.findAll();
        List<Berco> bercosCompativeis = new ArrayList<>();

        for (Berco berco : bercos) {

            if (blackListed) continue;

            if (accept.getCategoria() == null || berco.getCategoria() == null) continue;
            if (!Objects.equals(accept.getCategoria(), berco.getCategoria())) continue;

            if (accept.getLoa() == null || berco.getLoaMax() == null) continue;
            if (accept.getDwt() == null || berco.getDwt() == null) continue;
            if (accept.getCaladoEntrada() == null || berco.getCaladoMax() == null) continue;
            if (accept.getCaladoSaida() == null || berco.getCaladoMax() == null) continue;

            boolean ok =
                    accept.getLoa() <= berco.getLoaMax()
                            && accept.getDwt() <= berco.getDwt()
                            && accept.getCaladoEntrada() <= berco.getCaladoMax()
                            && accept.getCaladoSaida() <= berco.getCaladoMax();

            if (ok) bercosCompativeis.add(berco);
        }

        // 10) Detecta restrição (usuário mandou berços específicos)
        List<Long> solicitadosIds = Optional.ofNullable(acceptRequest.getBercosSelecionados())
                .orElse(Collections.emptyList());

        boolean hasRestrics = !solicitadosIds.isEmpty();

        // Busca berços solicitados por ID (CORRETO)
        List<Berco> bercosRestricao = hasRestrics
                ? bercoRepository.findAllById(solicitadosIds)
                : Collections.emptyList();

        // 11) Salva primeiro pra gerar ID REAL
        accept = acceptRepository.save(accept);
        Long acceptId = accept.getId();

        // Strings de nomes de berços
        String nomeBercosComp = bercosCompativeis.stream()
                .map(b -> String.valueOf(b.getNome()))
                .reduce("", (a, b) -> a.isEmpty() ? b : a + ", " + b);

        String nomeBercosRestr = bercosRestricao.stream()
                .map(b -> String.valueOf(b.getNome()))
                .reduce("", (a, b) -> a.isEmpty() ? b : a + ", " + b);

        // =========================================================
        // ✅ LÓGICA NOVA:
        // - ACEITE_COM_RESTRICAO: PROIBIDO NO SALVAR
        // - se hasRestrics => EM_PROCESSAMENTO (só em editar vira restrição)
        // =========================================================

        if (hasRestrics) {
            // mantém os berços compatíveis + os solicitados (sem duplicar)
            Set<Long> ids = new HashSet<>();
            for (Berco b : bercosCompativeis) if (b.getId() != null) ids.add(b.getId());
            for (Berco b : bercosRestricao) {
                if (b.getId() != null && !ids.contains(b.getId())) {
                    bercosCompativeis.add(b);
                    ids.add(b.getId());
                }
            }

            accept.setBercos(bercosCompativeis);

            // ✅ aqui NÃO pode ACEITE_COM_RESTRICAO, então vai pra processamento
            accept.setStatus(AceiteStatus.EM_PROCESSAMENTO);

            msg =
                    "ID DO ACEITE: " + acceptId + "\n" +
                            "IMO DO NAVIO: " + accept.getImo() + "\n" +
                            "CAUSA (SISTEMA): Usuário solicitou berços específicos (restrição) -> análise.\n" +
                            "BERÇOS SOLICITADOS (USUÁRIO): " + nomeBercosRestr + "\n" +
                            "BERÇOS COMPATÍVEIS (SISTEMA): " + nomeBercosComp + "\n" +
                            "STATUS (SISTEMA): EM_PROCESSAMENTO\n" +
                            "OBS DO USUÁRIO: " + accept.getObs() + "\n" +
                            "DATA/HORA: " + accept.getDataHoraAccept() + "\n" +
                            "USUÁRIO: ID=" + user.getId() + " | " + user.getEmail() + " | " + user.getName();

            emailService.enviarEmailTexto(
                    destinatario,
                    "Aceite do Navio " + accept.getVessel().getNome() + " - STATUS: EM PROCESSAMENTO",
                    msg
            );

        } else if (!blackListed && !bercosCompativeis.isEmpty()) {

            accept.setBercos(bercosCompativeis);
            accept.setStatus(AceiteStatus.ACEITO);

            msg =
                    "ID DO ACEITE: " + acceptId + "\n" +
                            "IMO DO NAVIO: " + accept.getImo() + "\n" +
                            "CAUSA (SISTEMA): Aceito automaticamente.\n" +
                            "BERÇOS COMPATÍVEIS (SISTEMA): " + nomeBercosComp + "\n" +
                            "STATUS (SISTEMA): ACEITO\n" +
                            "OBS DO USUÁRIO: " + accept.getObs() + "\n" +
                            "DATA/HORA: " + accept.getDataHoraAccept() + "\n" +
                            "USUÁRIO: ID=" + user.getId() + " | " + user.getEmail() + " | " + user.getName();

            emailService.enviarEmailTexto(
                    destinatario,
                    "Aceite do Navio " + accept.getVessel().getNome() + " - STATUS: ACEITO",
                    msg
            );

            emailService.enviarEmailTexto(
                    user.getEmail(),
                    "Aceite do Navio " + accept.getVessel().getNome() + " - STATUS: ACEITO",
                    msg
            );

        } else {

            accept.setStatus(AceiteStatus.EM_PROCESSAMENTO);

            String causa = blackListed
                    ? "Navio está na BLACK LIST."
                    : "Nenhum berço comporta o navio (categoria/LOA/DWT/calados).";

            msg =
                    "ID DO ACEITE: " + acceptId + "\n" +
                            "IMO DO NAVIO: " + accept.getImo() + "\n" +
                            "CAUSA (SISTEMA): " + causa + "\n" +
                            "STATUS (SISTEMA): EM_PROCESSAMENTO\n" +
                            "OBS DO USUÁRIO: " + accept.getObs() + "\n" +
                            "DATA/HORA: " + accept.getDataHoraAccept() + "\n" +
                            "USUÁRIO: ID=" + user.getId() + " | " + user.getEmail() + " | " + user.getName();

            emailService.enviarEmailTexto(
                    destinatario,
                    "Aceite do Navio " + accept.getVessel().getNome() + " - STATUS: EM PROCESSAMENTO",
                    msg
            );
        }

        // 12) salva status final + berços
        accept = acceptRepository.save(accept);

        AcceptResponse acceptResponse = acceptMapper.toAcceptResponse(accept);
        return acceptAssembler.toModel(acceptResponse);
    }
}