package br.com.laps.aceite.api.file;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class FileStorageService {

    private static final Logger log = Logger.getLogger(FileStorageService.class.getName());

    @Value("${contato.disco.raiz}")
    private String storageDirectory;

    @PostConstruct
    public void init() {
        File dir = new File(storageDirectory);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                log.log(Level.INFO, "Diretório de armazenamento criado: " + storageDirectory);
            } else {
                log.log(Level.SEVERE, "Falha ao criar diretório de armazenamento: " + storageDirectory);
            }
        } else {
            log.log(Level.INFO, "Diretório de armazenamento encontrado: " + storageDirectory);
        }
    }

    public void saveFile(MultipartFile fileToSave) throws IOException {
        if (fileToSave == null || fileToSave.getOriginalFilename() == null) {
            throw new NullPointerException("The file or filename is null");
        }

        Path rootPath = Paths.get(storageDirectory).toAbsolutePath().normalize();
        Path targetPath = rootPath.resolve(fileToSave.getOriginalFilename()).normalize();

        // Segurança: garantir que o arquivo alvo está dentro do diretório de
        // armazenamento
        if (!targetPath.startsWith(rootPath)) {
            throw new SecurityException("Tentativa de path traversal detectada: " + fileToSave.getOriginalFilename());
        }

        // Salva o arquivo (sobrescreve se já existir)
        Files.copy(fileToSave.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        log.log(Level.INFO, "Arquivo salvo em: " + targetPath);
    }

    public File getDownloadFile(String fileName) throws Exception {
        if (fileName == null) {
            throw new NullPointerException("FileName is null");
        }

        Path rootPath = Paths.get(storageDirectory).toAbsolutePath().normalize();
        Path filePath = rootPath.resolve(fileName).normalize();

        // Segurança: garantir que o arquivo está dentro do diretório de armazenamento
        if (!filePath.startsWith(rootPath)) {
            throw new SecurityException("Tentativa de path traversal detectada: " + fileName);
        }

        File fileToDownload = filePath.toFile();
        if (!fileToDownload.exists()) {
            throw new FileNotFoundException("Arquivo não encontrado: " + fileName);
        }

        return fileToDownload;
    }

}
