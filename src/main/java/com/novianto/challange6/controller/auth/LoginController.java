package com.novianto.challange6.controller.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;
import com.novianto.challange6.config.ConfigSecurity;
import com.novianto.challange6.dto.GoogleDto;
import com.novianto.challange6.dto.LoginDto;
import com.novianto.challange6.entity.User;
import com.novianto.challange6.repository.UserRepository;
import com.novianto.challange6.service.UserAuthService;
import com.novianto.challange6.service.mail.EmailSender;
import com.novianto.challange6.util.EmailTemplate;
import com.novianto.challange6.util.Response;
import com.novianto.challange6.util.SimpleStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.Map;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/user-login/")
public class LoginController {
    @Autowired
    private UserRepository userRepository;
    ConfigSecurity config = new ConfigSecurity();
    @Autowired
    public UserAuthService serviceReq;
    @Value("${expired.token.password.minute:}")
    private int expiredToken;
    @Autowired
    public Response response;
    @Value("${BASEURL:}")
    private String BASEURL;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Value("${AUTHURL:}")
    private String AUTHURL;
    @Autowired
    public RegisterController registerController;
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;
    @Autowired
    public EmailTemplate emailTemplate;
    @Autowired
    public EmailSender emailSender;
    @Value("${APPNAME:}")
    private String APPNAME;

    @PostMapping("/login")
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map> login(@Valid @RequestBody LoginDto loginDto) {
        Map map = serviceReq.login(loginDto);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @PostMapping("/signin_google")
    @ResponseBody
    public ResponseEntity<Map> repairGoogleSigninAction(@RequestBody GoogleDto parameters) throws IOException {

        Map<String, Object> map123 = new HashMap<>();
        if (StringUtils.isEmpty(parameters.getAccessToken())) {
            return new ResponseEntity<Map>(response.errorTemplateResponse("Token is required."), HttpStatus.OK);
        }
        String accessToken = parameters.getAccessToken();

        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
        System.out.println("access_token user=" + accessToken);
        Oauth2 oauth2 = new Oauth2.Builder(new NetHttpTransport(), new JacksonFactory(), credential).setApplicationName(
                "Oauth2").build();
        Userinfoplus profile = null;
        try {
            profile = oauth2.userinfo().get().execute();
        } catch (GoogleJsonResponseException e) {
            return new ResponseEntity<Map>(response.errorTemplateResponse(e.getDetails()), HttpStatus.BAD_GATEWAY);
        }
        profile.toPrettyString();
        User user = userRepository.findOneByUsername(profile.getEmail());
        if (null != user) {
            if (!user.isEnabled()) {
                return new ResponseEntity<Map>(response.errorTemplateResponse("Your Account is disable. Please chek your email for activation."), HttpStatus.OK);
            }
            String oldPassword = user.getPassword();
            System.out.println("password lama :" + user.getPassword());
            String pass = "Password123";
            if (!passwordEncoder.matches(pass, oldPassword)) {
                System.out.println("update password berhasil");
                user.setPassword(passwordEncoder.encode(pass));
                userRepository.save(user);
            }
            String url = AUTHURL + "?username=" + profile.getEmail() +
                    "&password=" + pass +
                    "&grant_type=password" +
                    "&client_id=my-client-web" +
                    "&client_secret=password";
            ResponseEntity<Map> response123 = restTemplateBuilder.build().exchange(url, HttpMethod.POST, null, new
                    ParameterizedTypeReference<Map>() {
                    });
            if (response123.getStatusCode() == HttpStatus.OK) {
                userRepository.save(user);
                map123.put("access_token", response123.getBody().get("access_token"));
                map123.put("token_type", response123.getBody().get("token_type"));
                map123.put("refresh_token", response123.getBody().get("refresh_token"));
                map123.put("expires_in", response123.getBody().get("expires_in"));
                map123.put("scope", response123.getBody().get("scope"));
                map123.put("jti", response123.getBody().get("jti"));
                map123.put("status", 200);
                map123.put("message", "Success");
                map123.put("type", "login");
                user.setPassword(oldPassword);
                User datUser = userRepository.save(user);
                map123.put("user", datUser);
                return new ResponseEntity<Map>(response.successResponse(map123), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<Map>(response.errorTemplateResponse("Username is not registered yet. Please contact admin."), HttpStatus.OK);
        }
        return new ResponseEntity<Map>(response.successResponse(map123), HttpStatus.OK);
    }
}


