package br.com.treinaweb.twjobs.api.aceites.controllers;

//import br.com.treinaweb.twjobs.api.ships.assemblers.SkillAssembler;
import br.com.treinaweb.twjobs.api.aceites.assemblers.AceiteAssembler;
import br.com.treinaweb.twjobs.api.aceites.dtos.AceiteRequest;
import br.com.treinaweb.twjobs.api.aceites.dtos.AceiteResponse;
import br.com.treinaweb.twjobs.api.aceites.mappers.AceiteMapper;
import br.com.treinaweb.twjobs.api.jobs.dtos.JobRequest;
import br.com.treinaweb.twjobs.core.exceptions.AceiteNotFoundException;
import br.com.treinaweb.twjobs.core.models.Aceite;
import br.com.treinaweb.twjobs.core.models.Vessel;
import br.com.treinaweb.twjobs.core.permissions.TWJobsPermissions;
import br.com.treinaweb.twjobs.core.repositories.AceiteRepository;
import br.com.treinaweb.twjobs.core.repositories.VesselRepository;
import br.com.treinaweb.twjobs.core.services.auth.SecurityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/aceites")
public class AceiteRestController {

    private final AceiteMapper aceiteMapper;
    private final AceiteAssembler aceiteAssembler;
    private final AceiteRepository aceiteRepository;
    private final SecurityService securityService;
    private final PagedResourcesAssembler<AceiteResponse> pagedResourcesAssembler;

    private final VesselRepository vesselRepository;

    @GetMapping
    public CollectionModel<EntityModel<AceiteResponse>> findAll(Pageable pageable) {
        var aceites = aceiteRepository.findAll(pageable)
                .map(aceiteMapper::toAceiteResponse);
        return pagedResourcesAssembler.toModel(aceites, aceiteAssembler);
    }

    @GetMapping("/{id}")
    public EntityModel<AceiteResponse> findById(@PathVariable Long id) {
        var aceite = aceiteRepository.findById(id)
                .orElseThrow(AceiteNotFoundException::new);
        var aceiteResponse = aceiteMapper.toAceiteResponse(aceite);
        return aceiteAssembler.toModel(aceiteResponse);
    }
    private final ModelMapper modelMapper;
    @PostMapping
    @TWJobsPermissions.IsCompany
    @ResponseStatus(code = HttpStatus.CREATED)
    public EntityModel<AceiteResponse> create(@RequestBody @Valid AceiteRequest aceiteRequest) {
        var aceite = aceiteMapper.toAceite(aceiteRequest);
        aceite.setUser(securityService.getCurrentUser());

        var navio = vesselRepository.findByImo(aceite.getImo());

       // aceite.setImo((long) 9470820);
//        var navio = vesselRepository.existsByImo(aceite.getImo());
        //var navio="Peróla Brazil";
//        aceite.setVessel(modelMapper.map(navio, Vessel.class));




        Vessel existingCustomer = navio.get();
        //String nameWeWanted = existingCustomer.getName();
        aceite.setVessel(existingCustomer);

        aceite = aceiteRepository.save(aceite);
        var aceiteResponse = aceiteMapper.toAceiteResponse(aceite);
        return aceiteAssembler.toModel(aceiteResponse);
    }

    @PutMapping("/{id}")
    @TWJobsPermissions.IsOwner
    public EntityModel<AceiteResponse> update(
            @RequestBody @Valid AceiteRequest aceiteRequest,
            @PathVariable Long id
    ) {
        var aceite = aceiteRepository.findById(id)
                .orElseThrow(AceiteNotFoundException::new);
        var aceiteData = aceiteMapper.toAceite(aceiteRequest);
        BeanUtils.copyProperties(aceiteData, aceite, "id", "company", "candidates");
        aceite = aceiteRepository.save(aceite);
        var aceiteResponse = aceiteMapper.toAceiteResponse(aceite);
        return aceiteAssembler.toModel(aceiteResponse);
    }

    @DeleteMapping("/{id}")
    @TWJobsPermissions.IsOwner
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var aceite = aceiteRepository.findById(id)
                .orElseThrow(AceiteNotFoundException::new);
        aceiteRepository.delete(aceite);
        return ResponseEntity.noContent().build();
    }

}