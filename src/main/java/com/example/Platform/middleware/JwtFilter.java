package com.example.Platform.middleware;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Kiểm tra nếu tiêu đề không rỗng và bắt đầu bằng "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Lấy phần JWT (bỏ qua tiền tố "Bearer ")
            jwt = authorizationHeader.substring(7);
            // Trích xuất tên người dùng từ JWT
            username = jwtUtil.extractUsername(jwt);
        }

        // Kiểm tra nếu tên người dùng không rỗng và chưa có xác thực trong SecurityContext hiện tại
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Tải chi tiết người dùng từ UserDetailsService
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Kiểm tra tính hợp lệ của JWT với chi tiết người dùng
            if (jwtUtil.validateToken(jwt, userDetails)) {
                // Tạo đối tượng UsernamePasswordAuthenticationToken
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // Đặt chi tiết xác thực vào đối tượng token
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Đặt thông tin xác thực vào SecurityContext
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        // Tiếp tục chuỗi bộ lọc
        chain.doFilter(request, response);
    }
}
