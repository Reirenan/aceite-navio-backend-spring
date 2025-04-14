package br.com.treinaweb.twjobs.api.users.mappers;

import br.com.treinaweb.twjobs.api.users.dtos.UserRequest;
import br.com.treinaweb.twjobs.api.users.dtos.UserResponse;
import br.com.treinaweb.twjobs.core.models.User;

public interface UserMapper {

    User toUser(UserRequest userRequest);
    UserResponse toUserResponse(User user);

}
