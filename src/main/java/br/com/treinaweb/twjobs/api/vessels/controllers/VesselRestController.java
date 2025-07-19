package br.com.treinaweb.twjobs.api.vessels.controllers;


import br.com.treinaweb.twjobs.api.accepts.dtos.AcceptRequest;
import br.com.treinaweb.twjobs.api.file.FileManagerController;
import br.com.treinaweb.twjobs.api.vessels.assemblers.VesselAssembler;
import br.com.treinaweb.twjobs.api.vessels.dtos.VesselRequest;
import br.com.treinaweb.twjobs.api.vessels.dtos.VesselResponse;
import br.com.treinaweb.twjobs.api.vessels.mappers.VesselMapper;
import br.com.treinaweb.twjobs.core.enums.Role;
import br.com.treinaweb.twjobs.core.enums.VeriStatus;
import br.com.treinaweb.twjobs.core.exceptions.AceiteNotFoundException;
import br.com.treinaweb.twjobs.core.exceptions.NegocioException;
import br.com.treinaweb.twjobs.core.exceptions.VesselNotFoundException;

import br.com.treinaweb.twjobs.core.models.Accept;
import br.com.treinaweb.twjobs.core.models.Disco;
import br.com.treinaweb.twjobs.core.models.User;
import br.com.treinaweb.twjobs.core.models.Vessel;
import br.com.treinaweb.twjobs.core.permissions.TWJobsPermissions;
import br.com.treinaweb.twjobs.core.repositories.VesselCustomRepository;
import br.com.treinaweb.twjobs.core.services.auth.SecurityService;

import br.com.treinaweb.twjobs.core.repositories.VesselRepository;
import br.com.treinaweb.twjobs.core.services.CadastroVesselService;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
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


import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.logging.Logger.global;
import static org.hibernate.type.descriptor.java.CoercionHelper.toLong;

//@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vessels")
public class VesselRestController {

    private final VesselAssembler vesselAssembler;
    private final VesselMapper vesselMapper;
    private final CadastroVesselService cadastroVesselService;
    private final VesselRepository vesselRepository;

    private final SecurityService securityService;
    private final PagedResourcesAssembler<VesselResponse> pagedResourcesAssembler;

    private final FileManagerController fileManagerController;
    private final VesselCustomRepository vesselCustomRepository;


    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private final Disco disco;

    @GetMapping("/sem-paginacao")
    public CollectionModel<EntityModel<VesselResponse>> findAllSemPaginacao() {
        List<VesselResponse> lista = vesselRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                .map(vesselMapper::toVesselResponse)
                .collect(Collectors.toList());
        return vesselAssembler.toCollectionModel(lista);
    }

    public void check_user(Vessel vessel){

        if(securityService.getCurrentUser() != vessel.getUser() ) {
            throw new NegocioException("Você não é proprietário do dado.");
        }
    }


    @GetMapping
    public CollectionModel<EntityModel<VesselResponse>> findAll(@PageableDefault(value = 15) Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").descending());

        User user = securityService.getCurrentUser();
        Long userId = user.getId();

//        if (user.getRole().equals(Role.COMPANY)) {
            // throw new NegocioException("É company");
            var vessels = vesselRepository.findAll(sortedPageable)
                    .map(vesselMapper::toVesselResponse);
            return pagedResourcesAssembler.toModel(vessels, vesselAssembler);
//        } else if (user.getRole().equals(Role.CANDIDATE)) {
//            // throw new NegocioException("É candidate");
//            var vessels = vesselRepository.findAllByUserId(pageable, userId)
//                    .map(vesselMapper::toVesselResponse);
//            return pagedResourcesAssembler.toModel(vessels, vesselAssembler);
//        }

//        return null;
        // return pagedResourcesAssembler.toModel(vessels, vesselAssembler);
    }


//          """
//          Esse método retorna apenas os Vessels do Usuário. role CANDIDATE pra cima.
//
//                           """
//    @GetMapping
//    public CollectionModel<EntityModel<VesselResponse>> findAll(@PageableDefault(value = 15) Pageable pageable) {
////
//        User user = securityService.getCurrentUser();
//        Long userId = user.getId();
//
////        Precisa do Optional
//        Page<VesselResponse>
//                vessels = vesselRepository.findAllByUserId(pageable,userId)
//                .map(vesselMapper::toVesselResponse);
////                   .orElseThrow(VesselNotFoundException::new);
//
//        return pagedResourcesAssembler.toModel(vessels, vesselAssembler);
//    }
//
//
////       """
////          Esse método retorna todos os Vessels. `apenas` role COMPANY
////
////                          """
//    @GetMapping("/admin")
//    @TWJobsPermissions.IsCompany
//    public CollectionModel<EntityModel<VesselResponse>> admFindAll(@PageableDefault(value = 15) Pageable pageable) {
//
//
//        var vessels = vesselRepository.findAll(pageable)
//                .map(vesselMapper::toVesselResponse);
////                  .orElseThrow(VesselNotFoundException::new);
//
//        return pagedResourcesAssembler.toModel(vessels, vesselAssembler);
//    }
//
//
//
//
//    @GetMapping("/{id}")
//    public EntityModel<VesselResponse> findById(@PathVariable Long id) {
//
//        var vessel = vesselRepository.findById(id)
//                .orElseThrow(VesselNotFoundException::new);
//
////        check_user(vessel);
//
//        var vesselResponse = vesselMapper.toVesselResponse(vessel);
//        return vesselAssembler.toModel(vesselResponse);
//    }

//    @GetMapping("/{id}")
//    public EntityModel<VesselResponse> findById(@PathVariable Long id) {
//
//        var vessel = vesselRepository.findById(id)
//                .orElseThrow(VesselNotFoundException::new);
//
//        check_user(vessel);
//
//        var vesselResponse = vesselMapper.toVesselResponse(vessel);
//        return vesselAssembler.toModel(vesselResponse);
//    }

    @GetMapping("statistics/count")
    public Long howMany() {
        return vesselRepository.countAllVessels();
    }


    @GetMapping("/custom")
    public List<Vessel> findTest(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "imo", required = false) Long imo,
            @RequestParam(value = "categoria", required = false) String categoria,
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "dataInicio", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(value = "dataFim", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        List<Vessel> vessels = vesselCustomRepository.vesselsCustom(id, imo, categoria, nome, dataInicio, dataFim);
        return vessels;
    }




    @Value("${contato.disco.raiz}")
    private String raiz;

    @Value("${contato.disco.diretorio-fotos}")
    private String diretorioFotos;


    private static final String STORAGE_DIRECTORY = "C:\\StoragePorto";
    
    @ResponseStatus(HttpStatus.CREATED)
//    @TWJobsPermissions.IsCompany
    @PostMapping
    public EntityModel<VesselResponse> create(@Valid @RequestParam(name="vesselRequestForm", required=true) String vesselRequestForm,
//                                              @RequestParam(name="foto", required=true) List<MultipartFile> fotos
                                              @RequestParam(name="foto", required=true) MultipartFile foto
    ) throws JsonProcessingException {



//        List<String> pathsList = new ArrayList<>();


        // Itera sobre a lista de arquivos enviados
//        for (MultipartFile foto : fotos) {
//            if (!foto.isEmpty()) {
////                     throw new NegocioException("Fotos não é vazio");
//                fileManagerController.uploadFile(foto);
////                pathsList.add(foto.getOriginalFilename());
//            }
//        }

        //verifica extensao
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

        fileManagerController.uploadFile(foto);



//        String originalfilename = foto.getOriginalFilename();
//
//        disco.salvarFoto(foto, securityService.getCurrentUser(), originalfilename.substring(originalfilename.lastIndexOf("/")+1));

        LocalDate date = LocalDate.now();

//        ObjectMapper mapper = new ObjectMapper();
//        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
//        VesselRequest vesselRequestOk = mapper.readValue(vesselRequest, new TypeReference<VesselRequest>(){});

        VesselRequest vesselRequest = mapper.readValue(vesselRequestForm, VesselRequest.class);

//        var vessel = vesselMapper.toVessel(vesselRequestForm);
        var vessel = vesselMapper.toVessel(vesselRequest);

//        if (!pathsList.isEmpty()) {
//            vessel.setPath(String.join("/%", pathsList)); // Adiciona todas as URLs, separadas por vírgula, por exemplo
//        }

        if(foto!=null) {
            vessel.setPath(foto.getOriginalFilename());
        }




//        vessel.setPath(raiz+"\\"+diretorioFotos+"\\"+securityService.getCurrentUser().getId()+"--"+String.valueOf(date)+"."+foto.getContentType());

        vessel.setUser(securityService.getCurrentUser());


       //Tem que adicionar a validação se o tamanho do IMO é igual à 8.

        int[] fator = {7, 6, 5, 4, 3, 2};


        Long l = vessel.getImo();
        String imo = Long.toString(l);

        int[] newImo = new int[imo.length()];
        for(int i = 0; i < imo.length(); i++) {
            newImo[i] = imo.charAt(i) - '0';
        }

        int lastNumber = newImo[6];

        int operation = (fator[0]*newImo[0])+(fator[1]*newImo[1])+(fator[2]*newImo[2])+(fator[3]*newImo[3])+(fator[4]*newImo[4])+(fator[5]*newImo[5]);
        String operation_str = Integer.toString(operation);

        int[] newOperation = new int[operation_str.length()];
        for(int i = 0; i < operation_str.length(); i++) {
            newOperation[i] = operation_str.charAt(i) - '0';
        }

        int nOpeLen = newOperation.length;

//        if(lastNumber != newOperation[nOpeLen -1]) {
//            throw new NegocioException("O IMO não segue o padrão");
//        }
        vessel.setSt_ver_vessel(VeriStatus.valueOf("N"));




        vessel = vesselRepository.save(vessel);
        var vesselResponse = vesselMapper.toVesselResponse(vessel);
        return vesselAssembler.toModel(vesselResponse);
    }


   /* @GetMapping
    @TWJobsPermissions.IsCompany
    public List<Vessel> listar() {

        return vesselRepository.findAll();
    }*/



  /*  @PutMapping("/{vesselId}")
    public ResponseEntity<Vessel> atualizar(@Valid @PathVariable Long vesselId, @Valid @RequestBody Vessel vessel) {

        if(!vesselRepository.existsById(vesselId)) {
            return ResponseEntity.notFound().build();
        }

        vessel.setId(vesselId);



        vessel.setSt_ver_vessel(VeriStatus.valueOf("N"));

        Vessel vesselAtualizado = cadastroVesselService.salvar(vessel);

        return ResponseEntity.ok(vesselAtualizado);


    }*/







    @PutMapping("/{id}")
//    @TWJobsPermissions.IsOwner
//    @TWJobsPermissions.IsCompany
    public EntityModel<VesselResponse> update(
//            @RequestBody @Valid VesselRequest vesselRequest,
//            @PathVariable Long id

            @Valid @RequestParam(name="vesselRequestForm", required=true) String vesselRequestForm, @PathVariable Long id,  @RequestParam(name="foto", required=false) MultipartFile foto
    ) throws JsonProcessingException {
        VesselRequest vesselRequest = mapper.readValue(vesselRequestForm, VesselRequest.class);

        //verifica extensao
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






        var vessel = vesselRepository.findById(id)
                .orElseThrow(AceiteNotFoundException::new);

        var vesselData = vesselMapper.toVessel(vesselRequest);
        vesselData.setId(id);





//        check_user(vessel);

//        vessel.setSt_ver_vessel(vessel.getSt_ver_vessel());
//        vessel.setStatus(vessel.getStatus());

//        var vesselData = vesselMapper.toVessel(vesselRequest);

        //Resolve o problema do update.
//        vesselData.setSt_ver_vessel(vessel.getSt_ver_vessel());
//        vesselData.setStatus(vessel.getStatus());

        //TEMP
        var path = vessel.getPath();
        //TEMP
        if(foto!=null) {
            vessel.setPath(foto.getOriginalFilename());
            fileManagerController.uploadFile(foto);
            //SE NÃO ENVIAR NENHUM ARQUIVO
        } else {
            vessel.setPath(path);
        }

        //TEMP
        BeanUtils.copyProperties(vesselData, vessel, "id");


//        if(vesselData.getPath()==null) {
//            vessel.setPath(path);
//        }


        //TEMP
        vessel = vesselRepository.save(vesselData);

//        vesselData.setId(id);
//        vessel = vesselRepository.save(vessel);

//        vessel = vesselRepository.save(vessel);

       var vesselResponse = vesselMapper.toVesselResponse(vessel);
        return vesselAssembler.toModel(vesselResponse);
    }

    @DeleteMapping("/{id}")
//    @TWJobsPermissions.IsOwner
    @TWJobsPermissions.IsCompany
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var vessel = vesselRepository.findById(id)
                .orElseThrow(VesselNotFoundException::new);



        vesselRepository.delete(vessel);
        return ResponseEntity.noContent().build();
    }

//    Continuar colocando os métodos e substituindo por vessels.
//    Depois corrigir os objetos que talvez ainda não existam
//    Testar o cadastro de um vessel pra ver se guarda o user_id
    
}
