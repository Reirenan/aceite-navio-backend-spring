package br.com.treinaweb.twjobs.api.accepts.controllers;

//import br.com.treinaweb.twjobs.api.ships.assemblers.SkillAssembler;
import br.com.treinaweb.twjobs.api.accepts.assemblers.AcceptAssembler;
import br.com.treinaweb.twjobs.api.accepts.dtos.AcceptRequest;
import br.com.treinaweb.twjobs.api.accepts.dtos.AcceptResponse;
import br.com.treinaweb.twjobs.api.accepts.mappers.AcceptMapper;
import br.com.treinaweb.twjobs.api.file.FileManagerController;
import br.com.treinaweb.twjobs.api.vessels.dtos.VesselResponse;
import br.com.treinaweb.twjobs.core.enums.Role;
import br.com.treinaweb.twjobs.core.enums.VeriStatus;
import br.com.treinaweb.twjobs.core.exceptions.AcceptNotFoundException;
import br.com.treinaweb.twjobs.core.exceptions.NegocioException;
import br.com.treinaweb.twjobs.core.models.Accept;
import br.com.treinaweb.twjobs.core.models.Berco;
import br.com.treinaweb.twjobs.core.models.User;
import br.com.treinaweb.twjobs.core.models.Vessel;
import br.com.treinaweb.twjobs.core.permissions.TWJobsPermissions;
import br.com.treinaweb.twjobs.core.repositories.AcceptCustomRepository;
import br.com.treinaweb.twjobs.core.repositories.AcceptRepository;
import br.com.treinaweb.twjobs.core.repositories.UserRepository;
import br.com.treinaweb.twjobs.core.repositories.VesselRepository;
import br.com.treinaweb.twjobs.core.service.CadastroAcceptService;
import br.com.treinaweb.twjobs.core.service.EmailService;
import br.com.treinaweb.twjobs.core.services.auth.SecurityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.NonNull;
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
import java.util.Objects;
import java.util.stream.Collectors;


//"""
// Importante, quando eu tenho certeza que o dado existe, não usar findById: https://www.baeldung.com/spring-data-findbyid-vs-getbyid
//            """


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

        if(user.getRole()==Role.COMPANY) {
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
//    renan.montenegro2018@gmail.com

    @PutMapping("/{id}")
    @TWJobsPermissions.IsCompany
    public EntityModel<AcceptResponse> update(
//            @RequestBody @Valid AcceptRequest acceptRequest,
//            @PathVariable Long id

            @Valid @RequestParam(name="acceptRequestForm", required=true) String acceptRequestForm, @PathVariable Long id,  @RequestParam(name="foto", required=false) MultipartFile foto
    ) throws JsonProcessingException {
/*
  Apenas admin podem setar manualmente o Status. O Candidate não pode.
  Adicionar essa funcionalidade.
  O mesmo para berços.
*/
        //verifica extensao
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
//        APENAS ADMIN PODEM ALTERAR OS DADOS DO ACEITE
//        if(user.getRole()==Role.COMPANY) {

        accept = acceptRepository.findById(id)
                .orElseThrow(AcceptNotFoundException::new);

        var acceptData = acceptMapper.toAccept(acceptRequest);
//            acceptData.setId(id);






//        if(user.getRole()!= Role.COMPANY &&(!Objects.equals(accept.getUser().getId(), userId))) {throw new NegocioException("Você não é proprietário.");}


//            if(acceptData.getStatus() != null) {
//
//            }
        acceptData.setData_update(String.valueOf(LocalDate.now()));

        acceptData.setTime_update(String.valueOf(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));


        var path = accept.getPath();

        acceptData.setPath(path);

        //se enviar arquivo, pega o nome dele
        if(foto!=null) {
            acceptData.setPath(foto.getOriginalFilename());
            fileManagerController.uploadFile(foto);
            //SE NÃO ENVIAR NENHUM ARQUIVO
        }

        // else {

        //     acceptData.setPath(path);
        // }

        //copia os campos que eu setar de uma accept para acceptData - ja preenche logo
        BeanUtils.copyProperties(acceptData, accept, "id", "dataAccept", "data_create", "time_accept", "time_create","vessel", "user", "bercos");

//            if(acceptData.getPath()==null) {
//                accept.setPath(path);
//            }



        accept = acceptRepository.save(accept);

//        } else {
//            throw new NegocioException("Não tem permissão.");
//        }

        var acceptResponse = acceptMapper.toAcceptResponse(accept);

        //<ALTERAÇÕES 05/01/2026[->>]>

        String destinatario_admin = String.valueOf(userRepository.findBySendEmail(Boolean.TRUE).get().getEmail());

        String nome_bercos_autori = "";
        for(Berco berco : accept.getBercos()) {
            // GUARDA O NOME DOS BERCOS
            nome_bercos_autori = nome_bercos_autori + berco.getNome() + ", ";
        }
        //<ALTERAÇÕES 22/11/25[->>] >



        String msg = "";

// ID DO ACEITE
        if (accept.getId() != null) {
            msg = msg + "ID DO ACEITE: " + accept.getId() + "\n\n";
        }

// IMO DO NAVIO
        if (accept.getVessel() != null && accept.getVessel().getImo() != null && !accept.getVessel().getImo().equals("")) {
            msg = msg + "IMO DO NAVIO: " + accept.getVessel().getImo() + "\n\n";
        }

// BERÇOS AUTORIZADOS
        if ("Y".equals(accept.getStatus()) && nome_bercos_autori != null && !nome_bercos_autori.equals("")) {
            msg = msg + "BERÇOS AUTORIZADOS: " + nome_bercos_autori + "\n\n";
        }

// STATUS ATUAL DO ACEITE
        if (accept.getStatus() != null) {
            msg = msg + "STATUS ATUAL DO ACEITE: " + traduzStatus(accept.getStatus()) + "\n\n";
        }

// COMENTÁRIO RESPOSTA (PORTO)
        if (accept.getRestricoes() != null && !accept.getRestricoes().equals("")) {
            msg = msg + "COMENTÁRIO RESPOSTA (PORTO): " + accept.getRestricoes() + "\n\n";
        }

// DATA E HORA
        if (accept.getData_update() != null && accept.getTime_update() != null) {
            msg = msg + "DATA E HORA DESTA RESPOSTA: "
                    + accept.getData_update() + ", " + accept.getTime_update();
        }



        emailService.enviarEmailTexto(accept.getUser().getEmail(), "Aceite do Navio " + accept.getVessel().getNome() +"  - RESPOSTA DA SOLICITAÇÃO DO USUÁRIO ", msg);
        // ENVIAR CÓPIA PARA A COACE ->
        emailService.enviarEmailTexto(destinatario_admin, "Aceite do Navio " + accept.getVessel().getNome() +"  - RESPOSTA DA SOLICITAÇÃO ", msg);



        //<ALTERAÇÕES 22/11/25[->>]>
        //<ALTERAÇÕES 05/01/2026[<<-]>


        return acceptAssembler.toModel(acceptResponse);

    }
    private String traduzStatus(String status) {
        switch (status) {
            case "Y": return "Navio Aceito";
            case "NE": return "Aceite Negado";
            case "YR": return "Aceito com Restrição";
            case "N": return "Em processamento";
            case "EM": return "Em avaliação";
            default: return "Status desconhecido";
        }
    }
    @DeleteMapping("/{id}")
//    @TWJobsPermissions.IsOwner
    @TWJobsPermissions.IsCompany
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var accept = acceptRepository.findById(id)
                .orElseThrow(AcceptNotFoundException::new);
        User user = securityService.getCurrentUser();
        Long userId = user.getId();
//      ~~~     Se vc não é COMPANY nem dono dos dados
//        if(user.getRole()!= Role.COMPANY &&(!Objects.equals(accept.getUser().getId(), userId))) {
//            throw new NegocioException("Você não é proprietário.");
//        }
        acceptRepository.delete(accept);
        return ResponseEntity.noContent().build();
    }

}



//        ~~~   DADOS FIXOS
//        acceptCurrent.setData_create(accept.getData_create());
//        acceptCurrent.setDataAccept(accept.getDataAccept());
//        acceptCurrent.setUser(accept.getUser());
//        acceptCurrent.setVessel(accept.getVessel());
////        ~~~   DADOS MUTÁVEIS
//        if(acceptCurrent.getStatus()==accept.getStatus()) {
//            acceptCurrent.setStatus(accept.getStatus())
//            ;
//        }
////        else if(acceptCurrent.getStatus()==Role.)
//
//        else if(user.getRole()!= Role.COMPANY){throw new NegocioException("Você não tem permissão para alterar o status do aceite.");}
//        if(Objects.equals(accept.getObs(), acceptCurrent.getObs())) {acceptCurrent.setObs(accept.getObs());}
//        if(Objects.equals(accept.getImo(), acceptCurrent.getImo())) {acceptCurrent.setImo(accept.getImo());}
//        if((user.getRole()==Role.COMPANY) && (acceptCurrent.getBercos()==accept.getBercos())) {acceptCurrent.setBercos(accept.getBercos());}