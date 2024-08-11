package com.example.Platform.response;


import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse extends BaseResponse {
    private String fullname;
    private String username;

    private String email;

    private String phoneNumber;

    private String address;

    private String password;

    private Date dateOfBirth;

    private int roleId;

}


