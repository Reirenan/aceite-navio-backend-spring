package br.com.laps.aceite.api.accepts.controllers;

import br.com.laps.aceite.api.accepts.assemblers.AcceptAssembler;
import br.com.laps.aceite.api.accepts.dtos.AcceptResponse;
import br.com.laps.aceite.api.accepts.mappers.AcceptMapper;
import br.com.laps.aceite.api.file.FileManagerController;
import br.com.laps.aceite.core.enums.AceiteStatus;
import br.com.laps.aceite.core.permissions.PortoUsersPermissions;
import br.com.laps.aceite.core.repositories.AcceptCustomRepository;
import br.com.laps.aceite.core.repositories.AcceptRepository;
import br.com.laps.aceite.core.repositories.UserRepository;
import br.com.laps.aceite.core.repositories.VesselRepository;
import br.com.laps.aceite.core.services.accept.CadastroAcceptService;
import br.com.laps.aceite.core.services.auth.SecurityService;
import br.com.laps.aceite.core.services.email.EmailService;
import br.com.laps.aceite.api.accepts.dtos.AcceptRequest;

import br.com.laps.aceite.core.enums.Role;
import br.com.laps.aceite.core.exceptions.AcceptNotFoundException;
import br.com.laps.aceite.core.exceptions.NegocioException;
import br.com.laps.aceite.core.models.Accept;
import br.com.laps.aceite.core.models.Berco;
import br.com.laps.aceite.core.models.User;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accepts")
public class AcceptRestController {

    private final AcceptMapper acceptMapper;
    private final AcceptAssembler acceptAssembler;
    private final AcceptRepository acceptRepository;
    private final SecurityService securityService;
    private final PagedResourcesAssembler<AcceptResponse> pagedResourcesAssembler;

    private final CadastroAcceptService cadastroAcceptService;

    private final AcceptCustomRepository acceptCustomRepository;
    private final VesselRepository vesselRepository;

    private final FileManagerController fileManagerController;

    // <ALTERAÇÕES 05/01/26[->>] >
    private final UserRepository userRepository;
    // <ALTERAÇÕES 05/01/26[<<-] >

    // <ALTERAÇÕES 22/11/25[->>] >

    @Autowired
    private EmailService emailService;

    // </ALTERAÇÕES 22/11/25[->>] >

    @Autowired
    private ObjectMapper mapper;
    //

    @GetMapping("statistics/count")
    public Long howMany() {
        return acceptRepository.countAllAccepts();
    }

    @GetMapping("/sem-paginacao")
    public CollectionModel<EntityModel<AcceptResponse>> findAllSemPaginacao() {
        List<AcceptResponse> lista = acceptRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                .map(acceptMapper::toAcceptResponse)
                .collect(Collectors.toList());
        return acceptAssembler.toCollectionModel(lista);
    }

    // "@PageableDefault(value = 7)" tamanho local para o tamanho da paginação. Deve
    // ser igual ou menor ao valor encontrado no "application.properties"
    @PortoUsersPermissions.IsAgenteNavio
    @Transactional(readOnly = true)
    @GetMapping
    public CollectionModel<EntityModel<AcceptResponse>> findAll(@PageableDefault(value = 15) Pageable pageable) {

        User user = securityService.getCurrentUser();
        Long userId = user.getId();

        Page<Accept> accepts;
        if (user.getRole() == Role.AGENTE_NAVIO) {
            accepts = acceptRepository.findAllByUserId(userId, pageable);
        } else {
            accepts = acceptRepository.findAll(pageable);
        }

        var acceptsResponse = accepts.map(acceptMapper::toAcceptResponse);
        return pagedResourcesAssembler.toModel(acceptsResponse, acceptAssembler);
    }

    @PortoUsersPermissions.IsAgenteNavio
    @GetMapping("/{id}")
    public EntityModel<AcceptResponse> findById(@PathVariable Long id) {
        User user = securityService.getCurrentUser();
        Long userId = user.getId();
        Accept accept;

        if (user.getRole() != Role.AGENTE_NAVIO) {
            accept = acceptRepository.findById(id)
                    .orElseThrow(AcceptNotFoundException::new);
        } else {
            accept = acceptRepository.findByIdAndUserId(id, userId)
                    .orElseThrow(AcceptNotFoundException::new);
        }

        var acceptResponse = acceptMapper.toAcceptResponse(accept);
        return acceptAssembler.toModel(acceptResponse);
    }

    @PortoUsersPermissions.IsAgenteNavio
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public EntityModel<AcceptResponse> create(
            @Valid @RequestParam(name = "acceptRequestForm", required = true) String acceptRequestForm,
            @RequestParam(name = "foto", required = false) MultipartFile foto) throws JsonProcessingException {
        return cadastroAcceptService.salvar(acceptRequestForm, foto, null);
    }

    @PortoUsersPermissions.IsFuncionarioCoace
    @PatchMapping("/{id}/approve")
    public EntityModel<AcceptResponse> approve(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String restricoes = body.get("restricoes");
        Accept accept = acceptRepository.findById(id).orElseThrow(AcceptNotFoundException::new);
        accept.setStatus(AceiteStatus.ACEITO);
        accept.setRestricoes(restricoes);
        accept.setDataHoraAccept(java.time.LocalDateTime.now());
        // For now, reuse update logic or just save. The CadastroAcceptService should
        // probably handle notifications.
        accept = acceptRepository.save(accept);
        return acceptAssembler.toModel(acceptMapper.toAcceptResponse(accept));
    }

    @PortoUsersPermissions.IsFuncionarioCoace
    @PatchMapping("/{id}/reject")
    public EntityModel<AcceptResponse> reject(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String restricoes = body.get("restricoes");
        Accept accept = acceptRepository.findById(id).orElseThrow(AcceptNotFoundException::new);
        accept.setStatus(AceiteStatus.NEGADO);
        accept.setRestricoes(restricoes);
        accept.setDataHoraAccept(java.time.LocalDateTime.now());
        accept = acceptRepository.save(accept);
        return acceptAssembler.toModel(acceptMapper.toAcceptResponse(accept));
    }

    @PutMapping("/{id}")
    @PortoUsersPermissions.IsFuncionarioCoace
    public EntityModel<AcceptResponse> update(
            @Valid @RequestParam(name = "acceptRequestForm", required = true) String acceptRequestForm,
            @PathVariable Long id, @RequestParam(name = "foto", required = false) MultipartFile foto)
            throws JsonProcessingException {

        AcceptRequest acceptRequest = mapper.readValue(acceptRequestForm, AcceptRequest.class);
        Accept accept = acceptRepository.findById(id).orElseThrow(AcceptNotFoundException::new);
        var acceptData = acceptMapper.toAccept(acceptRequest);

        if (foto != null) {
            accept.setPath(foto.getOriginalFilename());
            fileManagerController.uploadFile(foto);
        }

        BeanUtils.copyProperties(acceptData, accept, "id", "dataHoraAccept", "data_create", "time_accept",
                "time_create", "vessel", "user", "bercos", "codigo");

        accept = acceptRepository.save(accept);
        var acceptResponse = acceptMapper.toAcceptResponse(accept);
        return acceptAssembler.toModel(acceptResponse);
    }

    private String traduzStatus(AceiteStatus status) {

        if (status == null) {
            return "Status desconhecido";
        }

        switch (status) {
            case ACEITO:
                return "Navio Aceito";

            case NEGADO:
                return "Aceite Negado";

            case ACEITE_COM_RESTRICAO:
                return "Aceito com Restrição";

            case EM_PROCESSAMENTO:
                return "Em processamento";

            default:
                return "Status desconhecido";
        }
    }

    @DeleteMapping("/{id}")
    @PortoUsersPermissions.IsFuncionarioCoace
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var accept = acceptRepository.findById(id)
                .orElseThrow(AcceptNotFoundException::new);
        User user = securityService.getCurrentUser();
        Long userId = user.getId();

        acceptRepository.delete(accept);
        return ResponseEntity.noContent().build();
    }
}
