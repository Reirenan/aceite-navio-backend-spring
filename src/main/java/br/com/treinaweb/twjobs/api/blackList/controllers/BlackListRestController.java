package br.com.treinaweb.twjobs.api.blackList.controllers;

//import br.com.treinaweb.twjobs.api.ships.assemblers.SkillAssembler;
import br.com.treinaweb.twjobs.api.blackList.assemblers.BlackListAssembler;
import br.com.treinaweb.twjobs.api.blackList.dtos.BlackListRequest;
import br.com.treinaweb.twjobs.api.blackList.dtos.BlackListResponse;
import br.com.treinaweb.twjobs.api.blackList.mappers.BlackListMapper;
import br.com.treinaweb.twjobs.core.enums.Role;
import br.com.treinaweb.twjobs.core.enums.VeriStatus;
import br.com.treinaweb.twjobs.core.exceptions.AcceptNotFoundException;
import br.com.treinaweb.twjobs.core.exceptions.BlackListedNotFoundException;
import br.com.treinaweb.twjobs.core.models.Accept;
import br.com.treinaweb.twjobs.core.models.BlackList;
import br.com.treinaweb.twjobs.core.models.User;
import br.com.treinaweb.twjobs.core.permissions.TWJobsPermissions;
import br.com.treinaweb.twjobs.core.repositories.AcceptRepository;
import br.com.treinaweb.twjobs.core.repositories.BlackListCustomRepository;
import br.com.treinaweb.twjobs.core.repositories.BlackListRepository;
import br.com.treinaweb.twjobs.core.repositories.VesselRepository;
import br.com.treinaweb.twjobs.core.service.CadastroAcceptService;
import br.com.treinaweb.twjobs.core.service.EmailService;
import br.com.treinaweb.twjobs.core.services.auth.SecurityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


//"""
// Importante, quando eu tenho certeza que o dado existe, não usar findById: https://www.baeldung.com/spring-data-findbyid-vs-getbyid
//            """


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/black-list")
public class BlackListRestController {

    private final BlackListMapper blackListMapper;
    private final BlackListAssembler blackListAssembler;
    private final BlackListRepository blackListRepository;
    private final SecurityService securityService;
    private final PagedResourcesAssembler<BlackListResponse> pagedResourcesAssembler;

    private final CadastroAcceptService cadastroAcceptService;
    private final BlackListCustomRepository blackListCustomRepository;

    private final VesselRepository vesselRepository;


    //"@PageableDefault(value = 7)" tamanho local para o tamanho da paginação. Deve ser igual ou menor ao valor encontrado no "application.properties"
    @GetMapping
    @TWJobsPermissions.IsCompany
    public CollectionModel<EntityModel<BlackListResponse>> findAll(@PageableDefault(value = 15) Pageable pageable) {

        User user = securityService.getCurrentUser();
        Long userId = user.getId();

//        org.springframework.data.domain.Page<BlackListResponse> blackLists = null;
//        if (user.getRole().equals(Role.COMPANY)) {
//            throw new NegocioException("É company");
             var blackLists = blackListRepository.findAll(pageable)
                    .map(blackListMapper::toBlackListResponse);
             return pagedResourcesAssembler.toModel(blackLists, blackListAssembler);
//        } else if (user.getRole().equals(Role.CANDIDATE)) {
////            throw new NegocioException("É candidate");
//
//            blackLists = blackListRepository.findAllByUserId(pageable, userId)
//                    .map(blackListMapper::toBlackListResponse);
//            pagedResourcesAssembler.toModel(blackLists, blackListAssembler);
//
//        }


//        Page<AcceptResponse>
//                accepts = acceptRepository.findAllByUserId(pageable,userId)
//                .map(acceptMapper::toAcceptResponse);
//
//
//        return null;
//        return pagedResourcesAssembler.toModel(blackLists, blackListAssembler);
    }

    @GetMapping("/statistics/count")
    @TWJobsPermissions.IsCompany
    public Long howMany() {
        return blackListRepository.countAllBlackLists();
    }



    @GetMapping("/custom")
    @TWJobsPermissions.IsCompany
    public List<BlackListResponse> findTest(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "imo", required = false) String imo, @RequestParam(value = "data_create", required = false) String data_create, @RequestParam(value = "time_create", required = false) String time_create) {


        List<BlackListResponse> blackListeds = blackListCustomRepository.blackListedsCustom(id, imo, data_create, time_create)
          .stream()
                .map(blackListMapper::toBlackListResponse)
                .toList();

        return blackListeds;
    }


    @Autowired
    private EmailService emailService;


    @PostMapping
    @TWJobsPermissions.IsCompany
    @ResponseStatus(code = HttpStatus.CREATED)
    public EntityModel<BlackListResponse> create(@RequestBody @Valid BlackListRequest blackListRequest) {

        var blackList = blackListMapper.toBlackList(blackListRequest);

        var vessel = vesselRepository.findByImo(blackList.getImo());


//      """Setando as datas de alteração"""
        blackList.setData_blacklisted(String.valueOf(LocalDate.now()+" "+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
        blackList.setData_create(String.valueOf(LocalDate.now()+" "+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
        blackList.setData_update(String.valueOf(LocalDate.now()));

        blackList.setTime_blacklisted(String.valueOf(LocalDate.now()+" "+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
        blackList.setTime_create(String.valueOf(LocalDate.now()+" "+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
        blackList.setTime_update(String.valueOf(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));

//        blackList.setMmsi(vessel.get().getMmsi());
//        blackList.setNome(vessel.get().getNome());
//        blackList.setLoa(vessel.get().getLoa());
//        blackList.setBoca(vessel.get().getBoca());
//        blackList.setDwt(vessel.get().getDwt());
//        blackList.setPontal(vessel.get().getPontal());
//        blackList.setPonte_mfold(vessel.get().getPonte_mfold());
//        blackList.setMfold_quilha(vessel.get().getMfold_quilha());
//        if(vessel.get().getCategoria() != null) {
//            blackList.setCategoria(vessel.get().getCategoria());
//        } else {
//            blackList.setCategoria("nao informado");
//        }
//
//        blackList.setFlag(Integer.valueOf(vessel.get().getFlag()));
//        blackList.setCalado_entrada(vessel.get().getCalado_entrada());
//        blackList.setCalado_saida(vessel.get().getCalado_saida());

        blackList = blackListRepository.save(blackList);
        var vesselResponse = blackListMapper.toBlackListResponse(blackList);
        return blackListAssembler.toModel(vesselResponse);
    }






    @PutMapping("/{id}")
//    @TWJobsPermissions.IsOwner
    @TWJobsPermissions.IsCompany
    public EntityModel<BlackListResponse> update(
            @RequestBody @Valid BlackListRequest blackListRequest,
            @PathVariable Long id
    ) {


        var blackList = blackListRepository.findById(id)
                .orElseThrow(BlackListedNotFoundException::new);



        var BlackListData = blackListMapper.toBlackList(blackListRequest);
        BlackListData.setData_update(String.valueOf(LocalDate.now()));
        BlackListData.setTime_update(String.valueOf(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));

        BeanUtils.copyProperties(BlackListData, blackList, "id", "imo", "data_blacklisted","data_create","time_blacklisted","time_create");
        blackList = blackListRepository.save(blackList);
        var acceptResponse = blackListMapper.toBlackListResponse(blackList);
        return blackListAssembler.toModel(acceptResponse);
    }
//


//    @TWJobsPermissions.IsOwner
    @DeleteMapping("/{id}")
    @TWJobsPermissions.IsCompany
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var blackList = blackListRepository.findById(id)
                .orElseThrow(BlackListedNotFoundException::new);

        /*check_user(accept);*/

        blackListRepository.delete(blackList);
        return ResponseEntity.noContent().build();
    }
//
}