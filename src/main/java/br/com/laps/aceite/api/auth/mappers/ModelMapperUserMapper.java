package br.com.laps.aceite.api.auth.mappers;

import br.com.laps.aceite.api.auth.dtos.UserRequest;
import br.com.laps.aceite.api.auth.dtos.UserResponse;
import br.com.laps.aceite.core.models.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModelMapperUserMapper implements UserMapper {

    private final ModelMapper modelMapper;

    @Override
    public UserResponse toUserResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public User toUser(UserRequest userRequest) {
        return modelMapper.map(userRequest, User.class);
    }
    
}
