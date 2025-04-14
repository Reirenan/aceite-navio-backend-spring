package br.com.treinaweb.twjobs.api.vessels.controllers;

import br.com.treinaweb.twjobs.api.vessels.assemblers.VesselAssembler;
import br.com.treinaweb.twjobs.api.vessels.dtos.VesselResponse;
import br.com.treinaweb.twjobs.api.vessels.mappers.VesselMapper;
import br.com.treinaweb.twjobs.core.exceptions.VesselNotFoundException;
import br.com.treinaweb.twjobs.core.models.Vessel;
import br.com.treinaweb.twjobs.core.repositories.VesselRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vdatas")
public class VesselDataRestController {

    private final VesselRepository vesselRepository;
    private final VesselMapper vesselMapper;
    private final VesselAssembler vesselAssembler;

    @GetMapping("{imo}")
    EntityModel<VesselResponse> ReturnVDataByImo(@PathVariable Long imo){

        var vessel = vesselRepository.findByImo(imo)
                .orElseThrow(VesselNotFoundException::new);

        var vesselResponse = vesselMapper.toVesselResponse(vessel);


        return vesselAssembler.toModel(vesselResponse);
    }


}
