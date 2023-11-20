package com.novianto.challange6.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RegisterDto {
    @NotEmpty(message = "username is required.")
    private String username;
    @NotEmpty(message = "email is required.")
    private String emailAddress;
    @NotEmpty(message = "password is required.")
    private String password;
}
