package br.com.treinaweb.twjobs.api.navios;


import br.com.treinaweb.twjobs.core.models.Navio;
import br.com.treinaweb.twjobs.core.repositories.NavioRepository;
import br.com.treinaweb.twjobs.core.services.CadastroNavioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/navios")
public class NavioController {

    private final CadastroNavioService cadastroNavioService;
    private final NavioRepository navioRepository;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Navio adicionar(@Valid @RequestBody Navio navio) {


        return cadastroNavioService.salvar(navio);
    }


    @GetMapping
    public List<Navio> listar() {

        return navioRepository.findAll();
    }



    @PutMapping("/{navioId}")
    public ResponseEntity<Navio> atualizar(@Valid @PathVariable Long navioId, @Valid @RequestBody Navio navio) {

        if(!navioRepository.existsById(navioId)) {
            return ResponseEntity.notFound().build();
        }

        navio.setId(navioId);

        Navio navioAtualizado = cadastroNavioService.salvar(navio);

        return ResponseEntity.ok(navioAtualizado);


    }




}
