package br.com.laps.aceite.api.vessels.controllers;

import br.com.laps.aceite.api.vessels.assembler.VesselAssembler;
import br.com.laps.aceite.api.vessels.dtos.VesselResponse;
import br.com.laps.aceite.api.vessels.mappers.VesselMapper;
import br.com.laps.aceite.core.exceptions.VesselNotFoundException;
import br.com.laps.aceite.core.repositories.VesselRepository;
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
    EntityModel<VesselResponse> ReturnVDataByImo(@PathVariable String imo){

        var vessel = vesselRepository
                .findTopByImoOrderByIdDesc(imo)
                .orElseThrow(VesselNotFoundException::new);

        var vesselResponse = vesselMapper.toVesselResponse(vessel);


        return vesselAssembler.toModel(vesselResponse);
    }


}
