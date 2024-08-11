package com.example.Platform.convert;

import com.example.Platform.dto.UserDTO;
import com.example.Platform.entity.User;
import com.example.Platform.response.UserResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    private final TypeMap<UserDTO, User> userTypeMap;
    private final TypeMap<User, UserResponse> userUserResponseTypeMap;


    @Autowired
    public UserConverter(ModelMapper modelMapper) {
        // Setup for UpdateUserConverter
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        this.userTypeMap = modelMapper.createTypeMap(UserDTO.class, User.class);
        this.userTypeMap.addMappings(mapper -> mapper.skip(User::setId));

        this.userUserResponseTypeMap = modelMapper.createTypeMap(User.class, UserResponse.class);
        this.userUserResponseTypeMap.addMappings(mapper -> mapper.map(src -> src.getRole().getId(), UserResponse::setRoleId ));


    }

    public void updateEntity(UserDTO userDTO, User user) {
        this.userTypeMap.map(userDTO, user);
    }


    public UserResponse toResponse(User user) {
        UserResponse userResponse = new UserResponse();
        this.userUserResponseTypeMap.map(user, userResponse);
        return userResponse;
    }

}
