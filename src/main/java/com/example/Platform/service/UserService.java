package com.example.Platform.service;

import com.example.Platform.dto.UserDTO;
import com.example.Platform.dto.UserLoginDTO;
import com.example.Platform.entity.User;
import com.example.Platform.exception.DataNotFoundException;
import com.example.Platform.response.UserResponse;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface UserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException;

    User getUserByPhoneNumber(String phoneNumber);

    Authentication loginUser(UserLoginDTO userLoginDTO) throws Exception;

    void deleteUserById(Long id);


    User updateUser(Long id, UserDTO userDetails);

    UserResponse getUserDetail(String token);
}
