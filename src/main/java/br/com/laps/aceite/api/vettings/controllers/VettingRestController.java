package br.com.laps.aceite.api.vettings.controllers;

import br.com.laps.aceite.api.vettings.assembler.VettingAssembler;
import br.com.laps.aceite.api.vettings.dtos.VettingRequest;
import br.com.laps.aceite.api.vettings.dtos.VettingResponse;
import br.com.laps.aceite.api.vettings.mappers.VettingMapper;
import br.com.laps.aceite.core.models.Vetting;
import br.com.laps.aceite.core.permissions.PortoUsersPermissions;
import br.com.laps.aceite.core.repositories.VettingRepository;
import br.com.laps.aceite.core.services.auth.SecurityService;
import br.com.laps.aceite.core.services.vetting.CadastroVettingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vettings")
public class VettingRestController {

    private final VettingRepository vettingRepository;
    private final CadastroVettingService cadastroVettingService;
    private final VettingMapper vettingMapper;
    private final VettingAssembler vettingAssembler;
    private final SecurityService securityService;
    private final PagedResourcesAssembler<VettingResponse> pagedResourcesAssembler;

    @PortoUsersPermissions.IsAgenteNavio
    @GetMapping
    public CollectionModel<EntityModel<VettingResponse>> findAll(
            @PageableDefault(size = 15) Pageable pageable) {
        var user = securityService.getCurrentUser();
        var vettingsPage = vettingRepository.findAllByUserId(user.getId(), pageable)
                .map(vettingMapper::toVettingResponse);
        return pagedResourcesAssembler.toModel(vettingsPage, vettingAssembler);
    }

    @PortoUsersPermissions.IsAgenteNavio
    @GetMapping("/{id}")
    public EntityModel<VettingResponse> findById(@PathVariable Long id) {
        var vetting = cadastroVettingService.buscarOuFalhar(id);
        var vettingResponse = vettingMapper.toVettingResponse(vetting);
        return vettingAssembler.toModel(vettingResponse);
    }

    @PortoUsersPermissions.IsAgenteNavio
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<VettingResponse> create(@RequestBody @Valid VettingRequest vettingRequest) {
        var vetting = vettingMapper.toVetting(vettingRequest);
        vetting.setUser(securityService.getCurrentUser());
        vetting = cadastroVettingService.salvar(vetting);
        var vettingResponse = vettingMapper.toVettingResponse(vetting);
        return vettingAssembler.toModel(vettingResponse);
    }

    @PortoUsersPermissions.IsAgenteNavio
    @PutMapping("/{id}")
    public EntityModel<VettingResponse> update(
            @RequestBody @Valid VettingRequest vettingRequest,
            @PathVariable Long id) {
        var vettingAtual = cadastroVettingService.buscarOuFalhar(id);
        var vettingSource = vettingMapper.toVetting(vettingRequest);

        org.springframework.beans.BeanUtils.copyProperties(vettingSource, vettingAtual, "id", "user", "dataCreate");

        vettingAtual = cadastroVettingService.salvar(vettingAtual);
        var vettingResponse = vettingMapper.toVettingResponse(vettingAtual);
        return vettingAssembler.toModel(vettingResponse);
    }

    @PortoUsersPermissions.IsFuncionarioCoace
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cadastroVettingService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
