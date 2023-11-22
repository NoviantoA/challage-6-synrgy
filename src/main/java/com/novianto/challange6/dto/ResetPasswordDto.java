package com.novianto.challange6.dto;

import lombok.Data;

@Data
public class ResetPasswordDto {
    public String email;
    public String otp;
    public String newPassword;
}
