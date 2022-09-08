package com.io.realworld.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class UserSignupRequest {

    @NotBlank
    private String username;
    @Email
    private String email;
    @NotBlank
    private String password;
}
