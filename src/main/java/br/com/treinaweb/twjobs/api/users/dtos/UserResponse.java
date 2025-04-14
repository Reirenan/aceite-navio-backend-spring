package br.com.treinaweb.twjobs.api.users.dtos;

import br.com.treinaweb.twjobs.core.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String password;
    private Role role;
    private Boolean sendEmail;

}
