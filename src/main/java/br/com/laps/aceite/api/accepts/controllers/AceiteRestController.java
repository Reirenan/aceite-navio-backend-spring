package br.com.laps.aceite.api.accepts.controllers;

import br.com.laps.aceite.api.accepts.assemblers.AcceptAssembler;
import br.com.laps.aceite.api.accepts.dtos.AcceptResponse;
import br.com.laps.aceite.api.accepts.mappers.AcceptMapper;
import br.com.laps.aceite.core.exceptions.AceiteNotFoundException;
import br.com.laps.aceite.core.models.Accept;
import br.com.laps.aceite.core.repositories.AcceptRepository;
import br.com.laps.aceite.core.services.accept.CadastroAcceptService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/aceites")
public class AceiteRestController {

    private final AcceptRepository acceptRepository;
    private final AcceptMapper acceptMapper;
    private final AcceptAssembler acceptAssembler;
    private final CadastroAcceptService cadastroAcceptService;

    @PatchMapping("/{id}/aceitar")
    public EntityModel<AcceptResponse> aceitar(@PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        String restricoes = (body != null) ? body.get("restricoes") : null;
        return cadastroAcceptService.aceitar(id, restricoes);
    }

    @PatchMapping("/{id}/recusar")
    public EntityModel<AcceptResponse> recusar(@PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        String restricoes = (body != null) ? body.get("restricoes") : null;
        return cadastroAcceptService.recusar(id, restricoes);
    }
}
