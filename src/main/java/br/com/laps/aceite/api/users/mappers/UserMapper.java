package br.com.laps.aceite.api.mappers;

import br.com.laps.aceite.api.dtos.UserRequest;
import br.com.laps.aceite.api.dtos.UserResponse;
import br.com.treinaweb.twjobs.core.models.User;

public interface UserMapper {

    User toUser(UserRequest userRequest);
    UserResponse toUserResponse(User user);

}
