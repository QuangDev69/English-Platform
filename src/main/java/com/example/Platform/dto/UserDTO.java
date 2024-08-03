package com.example.Platform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    @NotBlank(message = "Full name is required!")
    private String fullName;

    @NotBlank(message = "user name is required!")
    private String username;

    @NotBlank(message = "Phone Number is required!")
    private String phoneNumber;

    private String address;

    @NotBlank(message = "Password cannot be blank!")
    private String password;

    @NotBlank(message = "Confirm Password cannot be blank!")
    private String confirmPassword;

    private boolean isActive;

    private Date dateOfBirth;

    private String email;

    @NotNull(message = "RoleId cannot be blank!")
    private Long roleId;

}
