package br.com.treinaweb.twjobs.api.ships.controllers;

//import br.com.treinaweb.twjobs.api.ships.assemblers.SkillAssembler;
import br.com.treinaweb.twjobs.api.skills.dtos.SkillResponse;
import br.com.treinaweb.twjobs.api.skills.mappers.SkillMapper;
import br.com.treinaweb.twjobs.core.exceptions.SkillNotFoundException;
import br.com.treinaweb.twjobs.core.models.Ship;
import br.com.treinaweb.twjobs.core.permissions.TWJobsPermissions;
import br.com.treinaweb.twjobs.core.repositories.ShipRepository;
import br.com.treinaweb.twjobs.core.repositories.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ships")
public class ShipRestController {

    private final ShipRepository shipRepository;

    @GetMapping
    @TWJobsPermissions.IsCompany
    public List<Ship> getAll(){

        return shipRepository.findAll();
    }

    @PostMapping
    @TWJobsPermissions.IsCompany
    @ResponseStatus(code = HttpStatus.CREATED)
    public Ship add(@RequestBody Ship ship) {
            return shipRepository.save(ship);
    }



  //  private final SkillMapper skillMapper;
//    private final SkillAssembler skillAssembler;
//    private final SkillRepository skillRepository;
//    private final PagedResourcesAssembler<SkillResponse> pagedResourcesAssembler;

//    @GetMapping
//    public CollectionModel<EntityModel<SkillResponse>> findAll(Pageable pageable) {
//        var skills = skillRepository.findAll(pageable)
//            .map(skillMapper::toSkillResponse);
//
//        return pagedResourcesAssembler.toModel(skills, skillAssembler);
//    }

//    @GetMapping("/{id}")
//    public EntityModel<SkillResponse> findById(@PathVariable Long id) {
//        var skill = skillRepository.findById(id)
//            .map(skillMapper::toSkillResponse)
//            .orElseThrow(SkillNotFoundException::new);
//
//        return skillAssembler.toModel(skill);
//    }

//    @PostMapping
//    @TWJobsPermissions.IsCompany
//    @ResponseStatus(code = HttpStatus.CREATED)
//    public EntityModel<SkillResponse> create(@Valid @RequestBody SkillRequest skillRequest) {
//        var skill = skillMapper.toSkill(skillRequest);
//        skill = skillRepository.save(skill);
//        var skillResponse = skillMapper.toSkillResponse(skill);
//        return skillAssembler.toModel(skillResponse);
//    }

//    @PutMapping("/{id}")
//    @TWJobsPermissions.IsCompany
//    public EntityModel<SkillResponse> update(
//        @PathVariable Long id,
//        @Valid @RequestBody SkillRequest skillRequest
//    ) {
//        var skill = skillRepository.findById(id)
//            .orElseThrow(SkillNotFoundException::new);
//        BeanUtils.copyProperties(skillRequest, skill, "id");
//        skill = skillRepository.save(skill);
//        var skillResponse = skillMapper.toSkillResponse(skill);
//        return skillAssembler.toModel(skillResponse);
//    }

//    @DeleteMapping("/{id}")
//    @TWJobsPermissions.IsCompany
//    public ResponseEntity<?> delete(@PathVariable Long id) {
//        var skill = skillRepository.findById(id)
//            .orElseThrow(SkillNotFoundException::new);
//        skillRepository.delete(skill);
//        return ResponseEntity.noContent().build();
//    }
    
}
