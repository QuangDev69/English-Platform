package com.example.Platform.service.serviceImpl;

import com.example.Platform.convert.UserConverter;
import com.example.Platform.dto.UserDTO;
import com.example.Platform.dto.UserLoginDTO;
import com.example.Platform.entity.Role;
import com.example.Platform.entity.User;
import com.example.Platform.exception.DataNotFoundException;
import com.example.Platform.repository.RoleRepository;
import com.example.Platform.repository.UserRepository;
import com.example.Platform.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserConverter userConverter;

    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException {
        Optional<Role> roleOpt = roleRepository.findById(userDTO.getRoleId());
        if (roleOpt.isEmpty()) {
            throw new DataNotFoundException("Role id not found!");
        }
        String phoneNumber = userDTO.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        String username = userDTO.getUsername();
        if(userRepository.existsByUsername(username)) {
            throw new DataIntegrityViolationException("Username already exists");
        }


        User user = User.builder()
                .fullname(userDTO.getFullname())
                .phoneNumber(userDTO.getPhoneNumber())
                .address(userDTO.getAddress())
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .isActive(userDTO.isActive())
                .dateOfBirth(userDTO.getDateOfBirth())
                .role(roleOpt.get())
                .build();

        return userRepository.save(user);
    }


    @Override
    public Authentication loginUser(UserLoginDTO userLoginDTO) throws Exception {
        String loginIdentifier = userLoginDTO.getUsername() != null ? userLoginDTO.getUsername() : userLoginDTO.getPhoneNumber();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginIdentifier, userLoginDTO.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return authentication;
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username, phone number, or password", e);
        }
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public User updateUser(Long id, UserDTO userDetails) {
        User existUser = userRepository.findById(id).orElseThrow(null);
        userConverter.updateEntity(userDetails, existUser);
        return existUser;
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }
}
