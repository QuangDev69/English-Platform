package com.example.Platform.service.serviceImpl;

import com.example.Platform.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    public void sendResetPasswordEmail(String email, String token) {
        String resetUrl = "http://localhost:8080/api/v1/reset-password?token=" + token;
        String subject = "Reset Password";
        String message = "Click the link to reset your password: " + resetUrl;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }
}
