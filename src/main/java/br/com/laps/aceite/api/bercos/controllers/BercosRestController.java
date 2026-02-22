package br.com.treinaweb.twjobs.api.bercos.controllers;


import br.com.treinaweb.twjobs.api.bercos.assemblers.BercosAssembler;
import br.com.treinaweb.twjobs.api.bercos.dtos.BercoRequest;
import br.com.treinaweb.twjobs.api.bercos.dtos.BercoResponse;
import br.com.treinaweb.twjobs.api.bercos.mappers.BercoMapper;
import br.com.treinaweb.twjobs.core.enums.VeriStatus;
import br.com.treinaweb.twjobs.core.exceptions.AceiteNotFoundException;
import br.com.treinaweb.twjobs.core.exceptions.NegocioException;
import br.com.treinaweb.twjobs.core.exceptions.BercoNotFoundException;
import br.com.treinaweb.twjobs.core.models.Berco;
import br.com.treinaweb.twjobs.core.models.Vessel;
import br.com.treinaweb.twjobs.core.permissions.TWJobsPermissions;
import br.com.treinaweb.twjobs.core.repositories.BercoCustomRepository;
import br.com.treinaweb.twjobs.core.repositories.BercoRepository;
import br.com.treinaweb.twjobs.core.repositories.VesselRepository;
import br.com.treinaweb.twjobs.core.services.CadastroVesselService;
import br.com.treinaweb.twjobs.core.services.auth.SecurityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bercos")
public class BercosRestController {

    private final BercosAssembler bercoAssembler;
    private final BercoMapper bercoMapper;
    private final CadastroVesselService cadastroVesselService;
    private final BercoRepository bercoRepository;

    private final SecurityService securityService;
    private final PagedResourcesAssembler<BercoResponse> pagedResourcesAssembler;
    private final VesselRepository vesselRepository;
    private final BercoCustomRepository bercoCustomRepository;


    @GetMapping
    public CollectionModel<EntityModel<BercoResponse>> findAll(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").descending());

        var bercos = bercoRepository.findAll(sortedPageable)
                .map(bercoMapper::toBercoResponse);
        return pagedResourcesAssembler.toModel(bercos, bercoAssembler);
    }
    @GetMapping("/sem-paginacao")
    public CollectionModel<EntityModel<BercoResponse>> findAllSemPaginacao() {
        List<BercoResponse> lista = bercoRepository.findAll().stream()
                .map(bercoMapper::toBercoResponse)
                .collect(Collectors.toList());
        return bercoAssembler.toCollectionModel(lista);
    }
    @GetMapping("/{id}")
    public EntityModel<BercoResponse> findById(@PathVariable Long id) {
        var berco = bercoRepository.findById(id)
                .orElseThrow(BercoNotFoundException::new);
        var bercoResponse = bercoMapper.toBercoResponse(berco);
        return bercoAssembler.toModel(bercoResponse);
    }

    @GetMapping("/custom")
    public List<BercoResponse> custom(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "categoria", required = false) String categoria, @RequestParam(value = "nome", required = false) String nome){


        List<BercoResponse> bercos = bercoCustomRepository.bercosCustom(id, categoria, nome)
                 .stream()
                .map(bercoMapper::toBercoResponse)
                .toList();

        return bercos;
    }

//    @GetMapping("statistics/count")
//    @TWJobsPermissions.IsCompany
//    public Long howManyAccepts() {
//        return bercoRepository.countAllBercos();
//    }

    @ResponseStatus(HttpStatus.CREATED)
    @TWJobsPermissions.IsCompany
    @PostMapping
    public EntityModel<BercoResponse> create(@Valid @RequestBody BercoRequest bercoRequest) {
        var berco = bercoMapper.toBerco(bercoRequest);

        berco = bercoRepository.save(berco);

        List<Vessel> navios = vesselRepository.findAll();
        for(Vessel i : navios){
            i.setSt_ver_vessel(VeriStatus.valueOf("N"));
        }

        var bercoResponse = bercoMapper.toBercoResponse(berco);
        return bercoAssembler.toModel(bercoResponse);
    }



    @PutMapping("/{id}")
//    @TWJobsPermissions.IsOwner
    @TWJobsPermissions.IsCompany
    public EntityModel<BercoResponse> update(
            @RequestBody @Valid BercoRequest bercoRequest,
            @PathVariable Long id
    ) {
        var berco = bercoRepository.findById(id)
                .orElseThrow(BercoNotFoundException::new);
        var bercoData = bercoMapper.toBerco(bercoRequest);
        BeanUtils.copyProperties(bercoData, berco, "id", "company", "candidates");
        berco = bercoRepository.save(berco);

        List<Vessel> navios = vesselRepository.findAll();
        for(Vessel i : navios){
            i.setSt_ver_vessel(VeriStatus.valueOf("N"));
        }


        var bercoResponse = bercoMapper.toBercoResponse(berco);
        return bercoAssembler.toModel(bercoResponse);
    }

    @DeleteMapping("/{id}")
//    @TWJobsPermissions.IsOwner
    @TWJobsPermissions.IsCompany
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var berco = bercoRepository.findById(id)
                .orElseThrow(BercoNotFoundException::new);
        bercoRepository.delete(berco);

        List<Vessel> navios = vesselRepository.findAll();
        for(Vessel i : navios){
            i.setSt_ver_vessel(VeriStatus.valueOf("N"));
        }



        return ResponseEntity.noContent().build();
    }

//    Continuar colocando os métodos e substituindo por vessels.
//    Depois corrigir os objetos que talvez ainda não existam
//    Testar o cadastro de um vessel pra ver se guarda o user_id
    
}
