package com.novianto.challange6.dto;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginDto {
    @NotEmpty(message = "Username or Email is required.")
    private String usernameOrEmail;

    @NotEmpty(message = "password is required.")
    private String password;
}
