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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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


    //<ALTERAÇÕES 05/01/26[->>] >
    private final UserRepository userRepository;
    //<ALTERAÇÕES 05/01/26[<<-] >

    //<ALTERAÇÕES 22/11/25[->>] >

    @Autowired
    private EmailService emailService;

    //</ALTERAÇÕES 22/11/25[->>] >

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

    //"@PageableDefault(value = 7)" tamanho local para o tamanho da paginação. Deve ser igual ou menor ao valor encontrado no "application.properties"
    @GetMapping
    public CollectionModel<EntityModel<AcceptResponse>> findAll(@PageableDefault(value = 15) Pageable pageable) {

        User user = securityService.getCurrentUser();
        Long userId = user.getId();


//        if (user.getRole().equals(Role.COMPANY)) {
//            throw new NegocioException("É company");
        var accepts = acceptRepository.findAll(pageable)
                // findAll(pageable)
                .map(acceptMapper::toAcceptResponse) ;
        return pagedResourcesAssembler.toModel(accepts, acceptAssembler);
//        } else if (user.getRole().equals(Role.CANDIDATE)) {
////            throw new NegocioException("É candidate");
//            var accepts = acceptRepository.findAllByUserId(pageable,userId)
//                    .map(acceptMapper::toAcceptResponse);
//            return pagedResourcesAssembler.toModel(accepts, acceptAssembler);
//
//        }


//        Page<AcceptResponse>
//                accepts = acceptRepository.findAllByUserId(pageable,userId)
//                .map(acceptMapper::toAcceptResponse);
//
//
//        AJEITAR
//        return null;
//        return pagedResourcesAssembler.toModel(accepts, acceptAssembler);
    }





    @GetMapping("/custom")
    public List<AcceptResponse> findTest(@RequestParam(value = "id", required = false) Long id,
                                         @RequestParam(value = "imo", required = false) String imo,
                                         @RequestParam(value = "status", required = false) String status,
                                         @RequestParam(value = "nome", required = false) String nome,
                                         @RequestParam(value = "categoria", required = false) String categoria,
                                         @RequestParam(value = "dataInicio", required = false)
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
                                         @RequestParam(value = "dataFim", required = false)
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        List<AcceptResponse> accepts = acceptCustomRepository.acceptsCustom(id, imo, status, nome, categoria, dataInicio,dataFim)
                .stream()
                .map(acceptMapper::toAcceptResponse)
                .collect(Collectors.toList());
        return accepts;
    }



    @GetMapping("/{id}")
    public EntityModel<AcceptResponse> findById(@PathVariable Long id) {
        User user = securityService.getCurrentUser();
        Long userId = user.getId();
        Accept accept;

        if(user.getRole()==Role.FUNCIONARIO_COACE) {
            accept = acceptRepository.findById(id)
                    .orElseThrow(AcceptNotFoundException::new);
        } else {
            accept = acceptRepository.findByIdAndUserId(id, userId)
                    .orElseThrow(AcceptNotFoundException::new);
        }


        var acceptResponse = acceptMapper.toAcceptResponse(accept);
        return acceptAssembler.toModel(acceptResponse);
    }



    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public EntityModel<AcceptResponse>
    create(@Valid @RequestParam(name="acceptRequestForm", required=true) String acceptRequestForm,  @RequestParam(name="foto", required=false) MultipartFile foto) throws JsonProcessingException {return cadastroAcceptService.salvar(acceptRequestForm, foto,"pauloacb2020@gmail.com");}

    @PutMapping("/{id}")
    @PortoUsersPermissions.IsFuncionarioCoace
    @PortoUsersPermissions.IsAdministrador
    public EntityModel<AcceptResponse> update(


            @Valid @RequestParam(name="acceptRequestForm", required=true) String acceptRequestForm, @PathVariable Long id,  @RequestParam(name="foto", required=false) MultipartFile foto
    ) throws JsonProcessingException {

        if(foto != null) {
            String filename = foto.getOriginalFilename();
            String extension = null;
            int dotIndex = filename.lastIndexOf(".");
            if (dotIndex >= 0) {
                extension = filename.substring(dotIndex + 1);
            }

            String[] extensions = {"txt", "zip", "pdf"};

            Boolean verifica = false;
            if(extension!=null) {
                for(String i : extensions){
                    if(i.equals(extension) ) {
                        verifica =true;
                        break;
                    }
                }

                if(!verifica){
                    throw new NegocioException(extension);
                }

            }
        }

        AcceptRequest acceptRequest = mapper.readValue(acceptRequestForm, AcceptRequest.class);

        User user = securityService.getCurrentUser();
        Long userId = user.getId();

        Accept accept = new Accept();


        accept = acceptRepository.findById(id)
                .orElseThrow(AcceptNotFoundException::new);

        var acceptData = acceptMapper.toAccept(acceptRequest);




        var path = accept.getPath();

        acceptData.setPath(path);

        if(foto!=null) {
            acceptData.setPath(foto.getOriginalFilename());
            fileManagerController.uploadFile(foto);
        }


        BeanUtils.copyProperties(acceptData, accept, "id", "dataAccept", "data_create", "time_accept", "time_create","vessel", "user", "bercos");

        accept = acceptRepository.save(accept);

        var acceptResponse = acceptMapper.toAcceptResponse(accept);

        String destinatario_admin = String.valueOf(userRepository.findBySendEmail(Boolean.TRUE).get().getEmail());

        String nome_bercos_autori = "";
        for(Berco berco : accept.getBercos()) {
            nome_bercos_autori = nome_bercos_autori + berco.getNome() + ", ";
        }


        String msg = "";

        if (accept.getId() != null) {
            msg = msg + "ID DO ACEITE: " + accept.getId() + "\n\n";
        }

        if (accept.getVessel() != null && accept.getVessel().getImo() != null && !accept.getVessel().getImo().equals("")) {
            msg = msg + "IMO DO NAVIO: " + accept.getVessel().getImo() + "\n\n";
        }

        if ("Y".equals(accept.getStatus()) && nome_bercos_autori != null && !nome_bercos_autori.equals("")) {
            msg = msg + "BERÇOS AUTORIZADOS: " + nome_bercos_autori + "\n\n";
        }

        if (accept.getStatus() != null) {
            msg = msg + "STATUS ATUAL DO ACEITE: " + traduzStatus(accept.getStatus()) + "\n\n";
        }

        if (accept.getRestricoes() != null && !accept.getRestricoes().equals("")) {
            msg = msg + "COMENTÁRIO RESPOSTA (PORTO): " + accept.getRestricoes() + "\n\n";
        }

        if (accept.getDataHoraAccept() != null) {
            msg = msg + "DATA E HORA DESTA RESPOSTA: "
                    + accept.getDataHoraAccept() + "\n\n";
        }



        emailService.enviarEmailTexto(accept.getUser().getEmail(), "Aceite do Navio " + accept.getVessel().getNome() +"  - RESPOSTA DA SOLICITAÇÃO DO USUÁRIO ", msg);
        // ENVIAR CÓPIA PARA A COACE ->
        emailService.enviarEmailTexto(destinatario_admin, "Aceite do Navio " + accept.getVessel().getNome() +"  - RESPOSTA DA SOLICITAÇÃO ", msg);


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
    @PortoUsersPermissions.IsAdministrador
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var accept = acceptRepository.findById(id)
                .orElseThrow(AcceptNotFoundException::new);
        User user = securityService.getCurrentUser();
        Long userId = user.getId();

        acceptRepository.delete(accept);
        return ResponseEntity.noContent().build();
    }

}
