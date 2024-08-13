package com.example.Platform.service.serviceImpl;

import com.example.Platform.entity.User;
import com.example.Platform.repository.UserRepository;
import com.example.Platform.service.EmailService;
import com.example.Platform.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final RedisTemplate<String, String> redisTemplate;



    @Override
    public void createPasswordResetToken(String email) {
        // Tìm kiếm người dùng dựa trên địa chỉ email.
        User user = userRepository.findByEmail(email);

        String token = UUID.randomUUID().toString();
        System.out.println(token);
        // Lấy một đối tượng ValueOperations từ RedisTemplate để lưu trữ dữ liệu dưới dạng key-value.
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        // Lưu token với giá trị là email của người dùng vào Redis, với thời gian sống là 5 phút.
        ops.set(token, user.getEmail(), Duration.ofMinutes(5));

        emailService.sendResetPasswordEmail(user.getEmail(), token);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        // Lấy đối tượng ValueOperations từ RedisTemplate để truy xuất dữ liệu từ Redis.
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        // Truy xuất email liên kết với token từ Redis.
        String email = ops.get(token);

        User user = userRepository.findByEmail(email);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        redisTemplate.delete(token);
    }


}
