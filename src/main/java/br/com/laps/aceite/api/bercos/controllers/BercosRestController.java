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
    private final BercoCustomRepository bercoCustomRepository;

    @PortoUsersPermissions.IsAgenteNavio
    @GetMapping
    public CollectionModel<EntityModel<BercoResponse>> findAll(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("id").descending());

        var bercos = bercoRepository.findAll(sortedPageable)
                .map(bercoMapper::toBercoResponse);
        return pagedResourcesAssembler.toModel(bercos, bercoAssembler);
    }

    @PortoUsersPermissions.IsAgenteNavio
    @GetMapping("/sem-paginacao")
    public CollectionModel<EntityModel<BercoResponse>> findAllSemPaginacao() {
        List<BercoResponse> lista = bercoRepository.findAll().stream()
                .map(bercoMapper::toBercoResponse)
                .collect(Collectors.toList());
        return bercoAssembler.toCollectionModel(lista);
    }

    @PortoUsersPermissions.IsAgenteNavio
    @GetMapping("/{id}")
    public EntityModel<BercoResponse> findById(@PathVariable Long id) {
        var berco = bercoRepository.findById(id)
                .orElseThrow(BercoNotFoundException::new);
        var bercoResponse = bercoMapper.toBercoResponse(berco);
        return bercoAssembler.toModel(bercoResponse);
    }

    @PortoUsersPermissions.IsAgenteNavio
    @GetMapping("/custom")
    public List<BercoResponse> custom(@RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "categoria", required = false) String categoria,
            @RequestParam(value = "nome", required = false) String nome) {

        List<BercoResponse> bercos = bercoCustomRepository.bercosCustom(id, categoria, nome)
                .stream()
                .map(bercoMapper::toBercoResponse)
                .toList();

        return bercos;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PortoUsersPermissions.IsFuncionarioCoace
    @PostMapping
    public EntityModel<BercoResponse> create(@Valid @RequestBody BercoRequest bercoRequest) {
        var berco = bercoMapper.toBerco(bercoRequest);

        berco = bercoRepository.save(berco);

        var bercoResponse = bercoMapper.toBercoResponse(berco);
        return bercoAssembler.toModel(bercoResponse);
    }

    @PutMapping("/{id}")
    @PortoUsersPermissions.IsFuncionarioCoace
    public EntityModel<BercoResponse> update(
            @RequestBody @Valid BercoRequest bercoRequest,
            @PathVariable Long id) {
        var berco = bercoRepository.findById(id)
                .orElseThrow(BercoNotFoundException::new);

        var bercoData = bercoMapper.toBerco(bercoRequest);

        // Corrigido: Ignorar campos internos se existirem e garantir salvamento correto
        // da entidade gerenciada
        BeanUtils.copyProperties(bercoData, berco, "id");

        berco = bercoRepository.save(berco);

        var bercoResponse = bercoMapper.toBercoResponse(berco);
        return bercoAssembler.toModel(bercoResponse);
    }

    @DeleteMapping("/{id}")
    @PortoUsersPermissions.IsFuncionarioCoace
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var berco = bercoRepository.findById(id)
                .orElseThrow(BercoNotFoundException::new);
        bercoRepository.delete(berco);

        return ResponseEntity.noContent().build();
    }

}
