package br.com.laps.aceite.api.dtos;

import br.com.treinaweb.twjobs.core.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String name;

    private String email;

    private String password;

    private Role role;

    private Boolean sendEmail;
}
