package br.com.laps.aceite.api.bercos.controllers;


import br.com.laps.aceite.api.bercos.assemblers.BercosAssembler;
import br.com.laps.aceite.api.bercos.dtos.BercoRequest;
import br.com.laps.aceite.api.bercos.dtos.BercoResponse;
import br.com.laps.aceite.api.bercos.mappers.BercoMapper;
import br.com.laps.aceite.core.enums.VeriStatus;
import br.com.laps.aceite.core.exceptions.BercoNotFoundException;
import br.com.laps.aceite.core.models.Vessel;
import br.com.laps.aceite.core.permissions.PortoUsersPermissions;
import br.com.laps.aceite.core.repositories.BercoCustomRepository;
import br.com.laps.aceite.core.repositories.BercoRepository;
import br.com.laps.aceite.core.repositories.VesselRepository;
import br.com.laps.aceite.core.services.auth.SecurityService;
import br.com.laps.aceite.core.services.navio.CadastroVesselService;

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

    @PortoUsersPermissions.IsFuncionarioCoace
    @PortoUsersPermissions.IsAdministrador
    @PortoUsersPermissions.IsAgenteNavio
    @GetMapping
    public CollectionModel<EntityModel<BercoResponse>> findAll(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").descending());

        var bercos = bercoRepository.findAll(sortedPageable)
                .map(bercoMapper::toBercoResponse);
        return pagedResourcesAssembler.toModel(bercos, bercoAssembler);
    }
    @PortoUsersPermissions.IsFuncionarioCoace
    @PortoUsersPermissions.IsAdministrador
    @PortoUsersPermissions.IsAgenteNavio
    @GetMapping("/sem-paginacao")
    public CollectionModel<EntityModel<BercoResponse>> findAllSemPaginacao() {
        List<BercoResponse> lista = bercoRepository.findAll().stream()
                .map(bercoMapper::toBercoResponse)
                .collect(Collectors.toList());
        return bercoAssembler.toCollectionModel(lista);
    }
    @PortoUsersPermissions.IsFuncionarioCoace
    @PortoUsersPermissions.IsAdministrador
    @PortoUsersPermissions.IsAgenteNavio
    @GetMapping("/{id}")
    public EntityModel<BercoResponse> findById(@PathVariable Long id) {
        var berco = bercoRepository.findById(id)
                .orElseThrow(BercoNotFoundException::new);
        var bercoResponse = bercoMapper.toBercoResponse(berco);
        return bercoAssembler.toModel(bercoResponse);
    }
    @PortoUsersPermissions.IsFuncionarioCoace
    @PortoUsersPermissions.IsAdministrador
    @PortoUsersPermissions.IsAgenteNavio
    @GetMapping("/custom")
    public List<BercoResponse> custom(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "categoria", required = false) String categoria, @RequestParam(value = "nome", required = false) String nome){


        List<BercoResponse> bercos = bercoCustomRepository.bercosCustom(id, categoria, nome)
                 .stream()
                .map(bercoMapper::toBercoResponse)
                .toList();

        return bercos;
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PortoUsersPermissions.IsFuncionarioCoace
    @PortoUsersPermissions.IsAdministrador
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
    @PortoUsersPermissions.IsFuncionarioCoace
    @PortoUsersPermissions.IsAdministrador
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
    @PortoUsersPermissions.IsFuncionarioCoace
    @PortoUsersPermissions.IsAdministrador
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
    
}
