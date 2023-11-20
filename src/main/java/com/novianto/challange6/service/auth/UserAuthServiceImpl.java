package com.novianto.challange6.service.auth;

import com.novianto.challange6.config.ConfigSecurity;
import com.novianto.challange6.dto.LoginDto;
import com.novianto.challange6.dto.RegisterDto;
import com.novianto.challange6.entity.User;
import com.novianto.challange6.entity.auth.Role;
import com.novianto.challange6.repository.RoleRepository;
import com.novianto.challange6.repository.UserRepository;
import com.novianto.challange6.service.UserAuthService;
import com.novianto.challange6.service.impl.UserServiceImpl;
import com.novianto.challange6.util.Response;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import java.security.Principal;
import java.util.*;

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
    @Autowired
    private Oauth2UserDetailsService userDetailsService;

    @Override
    public Map login(LoginDto loginDto) {
        try {
            Map<String, Object> map = new HashMap<>();
            User checkUser = userRepository.findOneByUsernameOrEmail(loginDto.getUsernameOrEmail());
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
            String url = baseUrl + "/oauth/token?username=" + loginDto.getUsernameOrEmail() +
                    "&password=" + loginDto.getPassword() +
                    "&grant_type=password" +
                    "&client_id=my-client-web" +
                    "&client_secret=password";
            ResponseEntity<Map> response = restTemplateBuilder.build().exchange(url, HttpMethod.POST, null, new ParameterizedTypeReference<Map>() {
            });
            if (response.getStatusCode() == HttpStatus.OK) {
                User user = userRepository.findOneByUsernameOrEmail(loginDto.getUsernameOrEmail());
                List<String> roles = new ArrayList<>();
                for (Role role : user.getRoles()) {
                    roles.add(role.getName());
                }
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

    @Override
    public Map getDetailProfile(Principal principal) {
        User idUser = getUserIdToken(principal, userDetailsService);
        try {
            User obj = userRepository.save(idUser);
            return templateResponse.successResponse(obj);
        } catch (Exception e) {
            return templateResponse.error(e, "500");
        }
    }

    @Override
    public Map registerManual(RegisterDto registerDto) {
        Map map = new HashMap();
        try {
            String[] roleNames = {"ROLE_CUSTOMER", "ROLE_READ", "ROLE_WRITE"};
            User user = new User();
            user.setId(UUID.randomUUID());
            user.setUsername(registerDto.getUsername());
            System.out.println(registerDto.getUsername());
            user.setEmailAddress(registerDto.getEmailAddress());

            String password = encoder.encode(registerDto.getPassword().replaceAll("\\s+", ""));
            List<Role> r = repoRole.findByNameIn(roleNames);

            user.setRoles(r);
            user.setPassword(password);
            User obj = repoUser.save(user);

            return templateResponse.successResponse(obj);

        } catch (Exception e) {
            logger.error("Eror registerManual=", e);
            return templateResponse.errorTemplateResponse("eror:" + e);
        }
    }

    @Override
    public Map registerByGoogle(RegisterDto registerDto) {
        Map map = new HashMap();
        try {
            String[] roleNames = {"ROLE_CUSTOMER", "ROLE_READ", "ROLE_WRITE"}; // ROLE DEFAULE
            User user = new User();
            user.setUsername(registerDto.getUsername().toLowerCase());
            user.setEmailAddress(registerDto.getEmailAddress());
            user.setEnabled(false);
            String password = encoder.encode(registerDto.getPassword().replaceAll("\\s+", ""));
            List<Role> r = repoRole.findByNameIn(roleNames);
            user.setRoles(r);
            user.setPassword(password);
            User obj = repoUser.save(user);
            return templateResponse.successResponse(obj);

        } catch (Exception e) {
            logger.error("Eror registerManual=", e);
            return templateResponse.errorTemplateResponse("eror:" + e);
        }
    }

    private User getUserIdToken(Principal principal, Oauth2UserDetailsService userDetailsService) {
        UserDetails user = null;
        String username = principal.getName();
        if (!StringUtils.isEmpty(username)) {
            user = userDetailsService.loadUserByUsername(username);
        }
        if (null == user) {
            throw new UsernameNotFoundException("User not found");
        }
        User idUser = userRepository.findOneByUsername(user.getUsername());
        if (null == idUser) {
            throw new UsernameNotFoundException("User name not found");
        }
        return idUser;
    }
}
