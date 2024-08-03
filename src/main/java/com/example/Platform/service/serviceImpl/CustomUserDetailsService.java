package com.example.Platform.service.serviceImpl;

import com.example.Platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String usernameOrPhoneNumber) throws UsernameNotFoundException {
        return userRepository.findByUsername(usernameOrPhoneNumber)
                .or(() -> userRepository.findByPhoneNumber(usernameOrPhoneNumber))
                .map(user -> new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or phone number: " + usernameOrPhoneNumber));
    }
}
