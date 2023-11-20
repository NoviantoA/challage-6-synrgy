package com.novianto.challange6.controller.auth;

import com.novianto.challange6.config.ConfigSecurity;
import com.novianto.challange6.dto.RegisterDto;
import com.novianto.challange6.entity.User;
import com.novianto.challange6.repository.UserRepository;
import com.novianto.challange6.service.UserAuthService;
import com.novianto.challange6.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/user-register/")
public class RegisterController {
    @Autowired
    private UserRepository userRepository;
    ConfigSecurity config = new ConfigSecurity();
    @Autowired
    public UserAuthService serviceReq;
    @Autowired
    public Response templateCRUD;

    @PostMapping("/register")
    public ResponseEntity<Map> saveRegisterManual(@Valid @RequestBody RegisterDto registerDto) throws RuntimeException {
        Map map = new HashMap();
        User user = userRepository.checkExistingUsernameOrEmail(registerDto.getUsername(), registerDto.getEmailAddress());
        if (null != user) {
            return new ResponseEntity<Map>(templateCRUD.errorTemplateResponse("Username sudah ada"), HttpStatus.OK);
        }
        map = serviceReq.registerManual(registerDto);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }
}
