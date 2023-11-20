package com.novianto.challange6.service;

import com.novianto.challange6.dto.LoginDto;

import java.util.Map;

public interface UserAuthService {
    public Map login(LoginDto loginDto);
}
