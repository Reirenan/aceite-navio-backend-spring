package br.com.laps.aceite.api.navios.controllers;

import br.com.treinaweb.twjobs.api.vessels.assemblers.VesselAssembler;
import br.com.treinaweb.twjobs.api.vessels.dtos.VesselResponse;
import br.com.treinaweb.twjobs.api.vessels.mappers.VesselMapper;
import br.com.treinaweb.twjobs.core.exceptions.VesselNotFoundException;
import br.com.treinaweb.twjobs.core.repositories.VesselRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vdatas")
public class VesselDataRestController {

    private final VesselRepository vesselRepository;
    private final VesselMapper vesselMapper;
    private final VesselAssembler vesselAssembler;

    @GetMapping("{imo}")
    EntityModel<VesselResponse> ReturnVDataByImo(@PathVariable Long imo){

        var vessel = vesselRepository
                .findTopByImoOrderByIdDesc(imo)
                .orElseThrow(VesselNotFoundException::new);

        var vesselResponse = vesselMapper.toVesselResponse(vessel);


        return vesselAssembler.toModel(vesselResponse);
    }


}
