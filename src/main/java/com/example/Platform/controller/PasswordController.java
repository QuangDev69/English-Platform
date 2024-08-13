package com.example.Platform.controller;

import com.example.Platform.dto.ResetPasswordDTO;
import com.example.Platform.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        passwordResetService.createPasswordResetToken(email);
        return ResponseEntity.ok("Reset password email has been sent.");
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        passwordResetService.resetPassword(resetPasswordDTO.getToken(), resetPasswordDTO.getNewPassword());
        return ResponseEntity.ok("Password has been reset successfully.");
    }
}
