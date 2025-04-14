package br.com.treinaweb.twjobs.core.repositories;

import br.com.treinaweb.twjobs.core.enums.EmailActivation;
import br.com.treinaweb.twjobs.core.models.EmailSend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailSendRepository extends JpaRepository<EmailSend, Long> {


    List<EmailSend> findAllByUserId(Long userId);

    Boolean existsByUserIdAndStatus(Long userId, EmailActivation emailStatus);

    Boolean existsByUserIdAndId(Long userId, Long id);

    EmailSend findByStatus(EmailActivation statusEmail);
}
