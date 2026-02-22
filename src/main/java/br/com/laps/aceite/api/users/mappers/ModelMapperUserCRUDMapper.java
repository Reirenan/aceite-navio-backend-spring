package br.com.laps.aceite.api.users.mappers;

import br.com.laps.aceite.api.users.dtos.UserRequest;
import br.com.laps.aceite.api.users.dtos.UserResponse;
import br.com.laps.aceite.core.models.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModelMapperUserCRUDMapper implements UserMapper {

    private final ModelMapper modelMapper;

    @Override
    public User toUser(UserRequest userRequest) {
        return modelMapper.map(userRequest, User.class);
    }

    @Override
    public UserResponse toUserResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }

}
