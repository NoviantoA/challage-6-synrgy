package com.novianto.challange6.service.auth;

import com.novianto.challange6.config.ConfigSecurity;
import com.novianto.challange6.dto.LoginDto;
import com.novianto.challange6.entity.User;
import com.novianto.challange6.entity.auth.Role;
import com.novianto.challange6.repository.RoleRepository;
import com.novianto.challange6.repository.UserRepository;
import com.novianto.challange6.service.UserAuthService;
import com.novianto.challange6.service.impl.UserServiceImpl;
import com.novianto.challange6.util.Response;
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
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    @Value("${BASEURL}")
    private String baseUrl;
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    ConfigSecurity config = new ConfigSecurity();
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private RoleRepository repoRole;
    @Autowired
    UserRepository repoUser;
    @Autowired
    public Response templateResponse;

    @Override
    public Map login(LoginDto loginDto) {
        try {
            Map<String, Object> map = new HashMap<>();
            User checkUser = userRepository.findOneByUsername(loginDto.getUsername());
            if ((checkUser != null) && (encoder.matches(loginDto.getPassword(), checkUser.getPassword()))) {
                if (!checkUser.isEnabled()) {
                    map.put("is_enabled", checkUser.isEnabled());
                    return templateResponse.errorTemplateResponse(map);
                }
            }
            if (checkUser == null) {
                return templateResponse.notFound("user not found");
            }
            if (!(encoder.matches(loginDto.getPassword(),
                    checkUser.getPassword()))) {
                return templateResponse.errorTemplateResponse("wrong password");
            }
            String url = baseUrl + "/oauth/token?username=" + loginDto.getUsername() +
                    "&password=" + loginDto.getPassword() +
                    "&grant_type=password" +
                    "&client_id=my-client-web" +
                    "&client_secret=password";
            ResponseEntity<Map> response = restTemplateBuilder.build().exchange(url, HttpMethod.POST, null, new ParameterizedTypeReference<Map>() {
            });
            if (response.getStatusCode() == HttpStatus.OK) {
                User user = userRepository.findOneByUsername(loginDto.getUsername());
                List<String> roles = new ArrayList<>();
                for (Role role : user.getRoles()) {
                    roles.add(role.getName());
                }
                //save token
                //checkUser.setAccessToken(response.getBody().get("access_token").toString());
                //checkUser.setRefreshToken(response.getBody().get("refresh_token").toString());
                // userRepository.save(checkUser);
                map.put("access_token", response.getBody().get("access_token"));
                map.put("token_type", response.getBody().get("token_type"));
                map.put("refresh_token", response.getBody().get("refresh_token"));
                map.put("expires_in", response.getBody().get("expires_in"));
                map.put("scope", response.getBody().get("scope"));
                map.put("jti", response.getBody().get("jti"));
                return map;
            } else {
                return templateResponse.notFound("user not found");
            }
        } catch (HttpStatusCodeException e) {
            e.printStackTrace();
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                return templateResponse.errorTemplateResponse("invalid login");
            }
            return templateResponse.errorTemplateResponse(e);
        } catch (Exception e) {
            e.printStackTrace();
            return templateResponse.errorTemplateResponse(e);
        }
    }
}
