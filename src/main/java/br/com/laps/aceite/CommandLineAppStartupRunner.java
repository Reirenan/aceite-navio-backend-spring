package br.com.laps.aceite;

import br.com.laps.aceite.core.enums.Role;
import br.com.laps.aceite.core.models.User;
import br.com.laps.aceite.core.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandLineAppStartupRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (!userRepository.existsByEmail("renan.montenegro2018@gmail.com")) {

            User admin = new User();
            admin.setName("Renan");
            admin.setEmail("renan.montenegro2018@gmail.com");
            admin.setPassword(passwordEncoder.encode("r110705"));
            admin.setRole(Role.ADMINISTRADOR);
            admin.setSendEmail(false);

            userRepository.save(admin);

            System.out.println("ADMIN CRIADO COM SUCESSO");
        }
    }
}