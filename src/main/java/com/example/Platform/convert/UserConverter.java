package com.example.Platform.convert;

import com.example.Platform.dto.UserDTO;
import com.example.Platform.entity.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    private final TypeMap<UserDTO, User> userTypeMap;

    @Autowired
    public UserConverter(ModelMapper modelMapper) {
        // Setup for UpdateUserConverter
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        this.userTypeMap = modelMapper.createTypeMap(UserDTO.class, User.class);
        this.userTypeMap.addMappings(mapper -> mapper.skip(User::setId));

    }

    public void updateEntity(UserDTO userDTO, User user) {
        this.userTypeMap.map(userDTO, user);
    }

    public User toEntity(UserDTO userDTO) {
        User user = new User();
        this.userTypeMap.map(userDTO, user);
        return user;
    }

}
