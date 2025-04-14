package br.com.treinaweb.twjobs.api.emailSend;


import br.com.treinaweb.twjobs.core.enums.EmailActivation;
import br.com.treinaweb.twjobs.core.exceptions.NegocioException;
import br.com.treinaweb.twjobs.core.models.EmailSend;
import br.com.treinaweb.twjobs.core.permissions.TWJobsPermissions;
import br.com.treinaweb.twjobs.core.repositories.EmailSendRepository;
import br.com.treinaweb.twjobs.core.services.auth.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/emailSend")
public class EmailSendRestController {

    private final EmailSendRepository emailSendRepository;

    private final SecurityService securityService;

//    COLOCAR DEPOIS COMO DEFAULT - STATUS = DISABLE
    @PostMapping
    @TWJobsPermissions.IsCompany
    @ResponseStatus(code = HttpStatus.CREATED)
    public EmailSend add(@RequestBody EmailSend emailSend) {


        emailSend.setUser(securityService.getCurrentUser());

        return emailSendRepository.save(emailSend);

    }

    @GetMapping
    @TWJobsPermissions.IsCompany
    public List<EmailSend> getAll() {

        return emailSendRepository.findAllByUserId(securityService.getCurrentUser().getId());
    }

    @PutMapping("/{id}")
    @TWJobsPermissions.IsCompany
    public EmailSend put(@PathVariable Long id,@RequestBody EmailSend emailSend) {
//        Se liga que no frontend, assim que o usuário clicar a requisição vai ser feita mediante
//        clique na check-box

//        Se for enable : checa se todos os outros estão disable
//            se estão: salva
//        se não, retorna um erro

          emailSend.setId(id);
          emailSend.setUser(securityService.getCurrentUser());
          Boolean check = emailSendRepository.existsByUserIdAndStatus(securityService.getCurrentUser().getId(), emailSend.getStatus());

          if(emailSend.getStatus() == EmailActivation.enable) {
              if(check) {
                 emailSend.setStatus(EmailActivation.disable);
                 throw new NegocioException("Você só pode ter 1 e-mail ativo");
              }
          }

          return emailSendRepository.save(emailSend);

    }


    @DeleteMapping("{id}")
    @TWJobsPermissions
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Boolean check = emailSendRepository.existsByUserIdAndId(securityService.getCurrentUser().getId(), id);

        if(check) {
//            return  null;

            emailSendRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
