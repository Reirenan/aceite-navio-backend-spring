package br.com.treinaweb.twjobs.api.fotos.controllers;

import br.com.treinaweb.twjobs.core.models.Disco;
import br.com.treinaweb.twjobs.core.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fotos")
public class FotosRestController {

    @Autowired
    private final Disco disco;


    @PostMapping
    public void upload(@RequestParam MultipartFile foto, User user, String filetype){
        String originalfilename = foto.getOriginalFilename();

        disco.salvarFoto(foto, user, originalfilename.substring(originalfilename.lastIndexOf(".")+1));

    }
}
