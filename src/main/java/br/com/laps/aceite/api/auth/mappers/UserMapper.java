package br.com.laps.aceite.api.auth.mappers;

import br.com.laps.aceite.api.auth.dtos.UserRequest;
import br.com.laps.aceite.api.auth.dtos.UserResponse;
import br.com.laps.aceite.core.models.User;

public interface UserMapper {

    UserResponse toUserResponse(User user);
    User toUser(UserRequest userRequest);
    
}
