package br.com.treinaweb.twjobs.core.service;


import br.com.treinaweb.twjobs.core.exceptions.NegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("$spring.mail.username")
    private String remetente;



    @Async
    public String enviarEmailTexto(String destinatario, String assunto, String mensagem){

//          throw new NegocioException("Teste.");
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(remetente);
            simpleMailMessage.setTo(destinatario);
            simpleMailMessage.setSubject(assunto);
            simpleMailMessage.setText(mensagem);
            javaMailSender.send(simpleMailMessage);
            return "Email enviado";
        } catch(Exception e){
            return "Erro ao tentar enviar e-mail" + e.getLocalizedMessage();
        }
    }




}
