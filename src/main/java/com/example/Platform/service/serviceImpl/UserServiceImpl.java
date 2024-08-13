package com.example.Platform.service.serviceImpl;

import com.example.Platform.convert.UserConverter;
import com.example.Platform.dto.UserDTO;
import com.example.Platform.dto.UserLoginDTO;
import com.example.Platform.entity.Role;
import com.example.Platform.entity.User;
import com.example.Platform.exception.DataNotFoundException;
import com.example.Platform.exception.PermissionDenyException;
import com.example.Platform.middleware.JwtUtil;
import com.example.Platform.repository.RoleRepository;
import com.example.Platform.repository.UserRepository;
import com.example.Platform.response.UserResponse;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserConverter userConverter;
    private final JwtUtil jwtUtil;


    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException {
        Optional<Role> roleOpt = roleRepository.findById(userDTO.getRoleId());
        if (roleOpt.isEmpty()) {
            throw new DataNotFoundException("Role id not found!");
        }
        String phoneNumber = userDTO.getPhoneNumber();
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        String username = userDTO.getUsername();
        if (userRepository.existsByUsername(username)) {
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
    public UserResponse getUserDetail(String token) {
        if (jwtUtil.isTokenExpired(token)) {
            throw new RuntimeException("Token is expire");
        }
        String phoneNumber = jwtUtil.extractPhoneNumber(token);
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user != null) {
            return userConverter.toResponse(user);
        } else throw new RuntimeException("User not found");
    }

    @Override
    public void forgotPassword(String email) {

    }


    @Override
    @Transactional
    public User updateUser(Long id, UserDTO userDTO) {
        User existUser = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User not found with id: " + id));

        String newPhone = userDTO.getPhoneNumber();
        if (!existUser.getPhoneNumber().equals(userDTO.getPhoneNumber()) &&  userRepository.existsByPhoneNumber(newPhone)) {
            throw new DataIntegrityViolationException("Phone number is already exists");
        }

        Role role = roleRepository.findById(userDTO.getRoleId()).orElseThrow(() -> new DataNotFoundException("Role not found with id: " + userDTO.getRoleId()));
        userConverter.updateEntity(userDTO, existUser);

        if(userDTO.getPassword() != null) {
            String newPassword = passwordEncoder.encode(userDTO.getPassword());
            existUser.setPassword(newPassword);
        }

        if (role.getId() == 2) {
            throw new PermissionDenyException("You cannot update the account to an Admin role!");
        }

        existUser.setRole(role);
        return userRepository.save(existUser);
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

//
//    @Override
//    public void forgotPassword(String email) {
//        User user = userRepository.findByEmail(email);
//
//        // Tạo token đặt lại mật khẩu
//        String token = UUID.randomUUID().toString();
//        PasswordResetToken resetToken = new PasswordResetToken(token, user, LocalDateTime.now().plusMinutes(15));
//        passwordResetTokenRepository.save(resetToken);
//
//        // Gửi email với liên kết đặt lại mật khẩu
//        sendResetPasswordEmail(user.getEmail(), token);
//    }
}
