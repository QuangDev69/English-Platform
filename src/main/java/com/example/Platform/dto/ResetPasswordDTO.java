package com.example.Platform.dto;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetPasswordDTO {

    private String token;
    private String newPassword;
}
