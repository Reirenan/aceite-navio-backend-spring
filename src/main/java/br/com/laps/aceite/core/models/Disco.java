package br.com.laps.aceite.core.models;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Objects;

@Component
public class Disco {

    @Value("${contato.disco.raiz}")
    private String raiz;

    @Value("${contato.disco.diretorio-fotos}")
    private String diretorioFotos;

    LocalDate date = LocalDate.now();



    public void salvarFoto(MultipartFile foto, User user, String filetype) {
        this.salvar(this.diretorioFotos, foto, user, filetype);
    }

    public void salvar(String diretorio, MultipartFile arquivo, User user, String filetype) {
        Path diretorioPath = Paths.get(this.raiz, diretorio);

        String filename = user.getId() +"--"+String.valueOf(date)+"."+filetype;

//        Path arquivoPath = diretorioPath.resolve(Objects.requireNonNull(arquivo.getOriginalFilename()));
        Path arquivoPath = diretorioPath.resolve(Objects.requireNonNull(filename));

        try {
            Files.createDirectories(diretorioPath);
            arquivo.transferTo(arquivoPath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Problemas na tentativa de salvar arquivo.", e);
        }
    }
}