package br.com.treinaweb.twjobs.api.file;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FileManagerController {


    @Autowired
    private FileStorageService fileStorageService;

    private static final Logger log = Logger.getLogger(FileManagerController.class.getName());

//    @PostMapping("/upload-file")
    public boolean uploadFile(
//            @RequestParam("file")
            MultipartFile file) {
        try {
            fileStorageService.saveFile(file);
            return true;
        } catch(Exception e) {
            log.log(Level.SEVERE, "Exception in file upload.", e);
        }
        return false;
    }


    @GetMapping("/download-file")
    public ResponseEntity<Resource> downloadFile(@RequestParam("filename") String filename) {
        log.log(Level.INFO, "[REGULAR] with /download-file");

        try {
            var fileToDownload = fileStorageService.getDownloadFile(filename);
            return ResponseEntity.ok()
                    .contentLength(fileToDownload.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"") // <-- CORRIGIDO AQUI
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(Files.newInputStream(fileToDownload.toPath())));
        } catch (Exception e) {
            log.log(Level.SEVERE, "Erro ao tentar baixar arquivo: " + filename, e);
            return ResponseEntity.notFound().build();
        }
    }



    //    ~~  DIVIDE FILE EM PARTES -> BAIXA PARALELO
    @GetMapping("/download-faster")
    public ResponseEntity<Resource> downloadFileFaster(
            @RequestParam("fileName")
            String filename) {
        log.log(Level.INFO, "[FASTER] with /download-faster");
        try{
            var fileToDownload = fileStorageService.getDownloadFile(filename);
            return ResponseEntity.ok()
                    .contentLength(fileToDownload.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName =\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new FileSystemResource(fileToDownload));

        } catch(Exception e) {

            return ResponseEntity.notFound().build();
        }


//         """   PERMITIR QUE SEJA UMA URL NORMAL PARA QUE CONFORME O USUÁRIO CLIQUE AQUELE ARQUIVO ESPECIFICO SEJA BAIXADO
//         """   E não uma requisição baixe arquivo por arquivo.

//         """   Enviar email pro user. (I)Para accept  /  (II)Para cadastro de usuário.
//         (I)https://youtu.be/_MwdIaMy_Ao?si=V-xn0Rb8nkUow97j  (II)https://youtu.be/V-ABkNuubaI?si=83K-Ejfjxs7Gy3if

    }



}
