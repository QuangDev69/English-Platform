package com.example.Platform.service.serviceImpl;

import com.example.Platform.entity.User;
import com.example.Platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrPhone) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(usernameOrPhone);
        if (user == null) {
            user = userRepository.findByPhoneNumber(usernameOrPhone);
            if (user == null) {
                throw new UsernameNotFoundException("User not found with username or phone: " + usernameOrPhone);
            }
        }
        return user;
    }
}

