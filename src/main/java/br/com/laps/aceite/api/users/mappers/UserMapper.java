package br.com.laps.aceite.api.users.mappers;

import br.com.laps.aceite.api.users.dtos.UserRequest;
import br.com.laps.aceite.api.users.dtos.UserResponse;
import br.com.laps.aceite.core.models.User;

public interface UserMapper {

    User toUser(UserRequest userRequest);
    UserResponse toUserResponse(User user);

}
