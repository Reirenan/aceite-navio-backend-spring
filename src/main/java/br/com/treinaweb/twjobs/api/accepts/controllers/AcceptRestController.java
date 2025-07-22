package br.com.treinaweb.twjobs.api.accepts.controllers;

//import br.com.treinaweb.twjobs.api.ships.assemblers.SkillAssembler;
import br.com.treinaweb.twjobs.api.accepts.assemblers.AcceptAssembler;
import br.com.treinaweb.twjobs.api.accepts.dtos.AcceptRequest;
import br.com.treinaweb.twjobs.api.accepts.dtos.AcceptResponse;
import br.com.treinaweb.twjobs.api.accepts.mappers.AcceptMapper;
import br.com.treinaweb.twjobs.api.bercos.dtos.BercoResponse;
import br.com.treinaweb.twjobs.api.file.FileManagerController;
import br.com.treinaweb.twjobs.core.enums.Role;
import br.com.treinaweb.twjobs.core.enums.VeriStatus;
import br.com.treinaweb.twjobs.core.exceptions.AcceptNotFoundException;
import br.com.treinaweb.twjobs.core.exceptions.NegocioException;
import br.com.treinaweb.twjobs.core.models.Accept;
import br.com.treinaweb.twjobs.core.models.User;
import br.com.treinaweb.twjobs.core.models.Vessel;
import br.com.treinaweb.twjobs.core.permissions.TWJobsPermissions;
import br.com.treinaweb.twjobs.core.repositories.AcceptCustomRepository;
import br.com.treinaweb.twjobs.core.repositories.AcceptRepository;
import br.com.treinaweb.twjobs.core.repositories.VesselRepository;
import br.com.treinaweb.twjobs.core.service.CadastroAcceptService;
import br.com.treinaweb.twjobs.core.service.EmailService;
import br.com.treinaweb.twjobs.core.services.auth.SecurityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

    @Autowired
    private ObjectMapper mapper;




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

    @GetMapping
    public CollectionModel<EntityModel<AcceptResponse>> findAll(@PageableDefault(size = 15) Pageable pageable) {

        Pageable ordenado = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("id").descending());

        var accepts = acceptRepository.findAll(ordenado)
                .map(acceptMapper::toAcceptResponse);

        return pagedResourcesAssembler.toModel(accepts, acceptAssembler);
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
            @Valid @RequestParam(name = "acceptRequestForm", required = true) String acceptRequestForm,
            @PathVariable Long id,
            @RequestParam(name = "foto", required = false) MultipartFile foto
    ) throws JsonProcessingException {

        // Verifica extensão do arquivo (se houver)
        String filename = foto.getOriginalFilename();
        String extension = null;
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex >= 0) {
            extension = filename.substring(dotIndex + 1);
        }

        String[] extensions = {"txt", "zip", "pdf"};
        Boolean verifica = false;
        if (extension != null) {
            for (String i : extensions) {
                if (i.equals(extension)) {
                    verifica = true;
                    break;
                }
            }

            if (!verifica) {
                throw new NegocioException(extension);
            }
        }

        AcceptRequest acceptRequest = mapper.readValue(acceptRequestForm, AcceptRequest.class);

        User user = securityService.getCurrentUser();
        Long userId = user.getId();

        Accept accept = acceptRepository.findById(id)
                .orElseThrow(AcceptNotFoundException::new);

        var acceptData = acceptMapper.toAccept(acceptRequest);

        acceptData.setData_update(String.valueOf(LocalDate.now()));
        acceptData.setTime_update(String.valueOf(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));

        if (acceptData.getTime_accept() == null || acceptData.getTime_accept().isBlank()) {
            String valorExistente = accept.getTime_accept();
            if (valorExistente != null && !valorExistente.isBlank()) {
                acceptData.setTime_accept(valorExistente);
            } else {
                acceptData.setTime_accept(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            }
        }

        var path = accept.getPath();

        if (foto != null) {
            acceptData.setPath(foto.getOriginalFilename());
            fileManagerController.uploadFile(foto);
        } else {
            accept.setPath(path);
        }

        // Copia campos definidos de acceptData para accept (exceto campos protegidos)
        BeanUtils.copyProperties(acceptData, accept,
                "id", "dataAccept", "data_create", "time_create", "vessel", "user", "bercos");


        accept = acceptRepository.save(accept);

        var acceptResponse = acceptMapper.toAcceptResponse(accept);
        return acceptAssembler.toModel(acceptResponse);
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