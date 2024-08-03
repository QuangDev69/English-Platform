package com.example.Platform.service.serviceImpl;

import com.example.Platform.convert.UserConverter;
import com.example.Platform.dto.UserDTO;
import com.example.Platform.entity.Role;
import com.example.Platform.entity.User;
import com.example.Platform.exception.DataNotFoundException;
import com.example.Platform.repository.RoleRepository;
import com.example.Platform.repository.UserRepository;
import com.example.Platform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException {
        Optional<Role> roleOpt = roleRepository.findById(userDTO.getRoleId());
        if (roleOpt.isEmpty()) {
            throw new DataNotFoundException("Role id not found!");
        }
        User user = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .address(userDTO.getAddress())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword()) // Mã hóa mật khẩu
                .isActive(userDTO.isActive())
                .dateOfBirth(userDTO.getDateOfBirth())
                .role(roleOpt.get())
                .build();

        return userRepository.save(user);
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new DataNotFoundException("User with phone number not found!"));
    }
}
