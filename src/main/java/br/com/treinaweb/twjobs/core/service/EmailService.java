package br.com.treinaweb.twjobs.core.service;


import br.com.treinaweb.twjobs.core.exceptions.NegocioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("$spring.mail.username")
    private String remetente;



    @Async
    public void enviarEmailTexto(String destinatario, String assunto, String mensagem) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(remetente);
            simpleMailMessage.setTo(destinatario);
            simpleMailMessage.setSubject(assunto);
            simpleMailMessage.setText(mensagem);

            javaMailSender.send(simpleMailMessage);

            log.info("Email enviado com sucesso para: {}", destinatario);
        } catch (Exception e) {
            log.error("Erro ao enviar email para o destinatário {}: {}", destinatario, e.getMessage(), e);
        }
    }





}
