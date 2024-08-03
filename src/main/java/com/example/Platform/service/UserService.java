package com.example.Platform.service;

import com.example.Platform.dto.UserDTO;
import com.example.Platform.entity.User;
import com.example.Platform.exception.DataNotFoundException;

public interface UserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException;
    User getUserByPhoneNumber(String phoneNumber);

}
