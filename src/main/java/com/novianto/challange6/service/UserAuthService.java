package com.novianto.challange6.service;

import com.novianto.challange6.dto.LoginDto;
import com.novianto.challange6.dto.RegisterDto;

import java.security.Principal;
import java.util.Map;

public interface UserAuthService {
    public Map login(LoginDto loginDto);
    public Map getDetailProfile(Principal principal);
    Map registerManual(RegisterDto registerDto);
    Map registerByGoogle(RegisterDto registerDto) ;
}
