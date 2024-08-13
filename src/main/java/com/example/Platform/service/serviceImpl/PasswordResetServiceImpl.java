package com.example.Platform.service.serviceImpl;

import com.example.Platform.entity.PasswordResetToken;
import com.example.Platform.entity.User;
import com.example.Platform.repository.PasswordResetTokenRepository;
import com.example.Platform.repository.UserRepository;
import com.example.Platform.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Override
    public void createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email);

        // Tạo token đặt lại mật khẩu
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user, LocalDateTime.now().plusMinutes(15));
        passwordResetTokenRepository.save(resetToken);

        // Gửi email với liên kết đặt lại mật khẩu
        sendResetPasswordEmail(user.getEmail(), token);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.isExpired()) {
            throw new RuntimeException("Token is expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Xóa token sau khi đã sử dụng
        passwordResetTokenRepository.delete(resetToken);
    }

    private void sendResetPasswordEmail(String email, String token) {
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
