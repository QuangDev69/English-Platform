package com.example.Platform.service;

public interface EmailService {
    void sendResetPasswordEmail(String email, String token);

}
