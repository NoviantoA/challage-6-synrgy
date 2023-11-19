package com.novianto.challange6.service.auth;

import com.novianto.challange6.entity.User;
import com.novianto.challange6.entity.auth.Client;
import com.novianto.challange6.entity.auth.Role;
import com.novianto.challange6.entity.auth.RolePath;
import com.novianto.challange6.repository.*;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Service
public class DatabaseSeeder implements ApplicationRunner {
    private static final String TAG = "DatabaseSeeder {}";
    private Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RolePathRepository rolePathRepository;
    private String defaultPassword = "password";
    private String[] users = new String[]{
            "user@mail.com:ROLE_CUSTOMER",
            "merchant@mail.com:ROLE_MERCHANT",
    };

    private String[] clients = new String[]{
            "my-client-apps:ROLE_READ ROLE_WRITE",
            "my-client-web:ROLE_READ ROLE_WRITE"
    };
    private String[] roles = new String[]{
            "ROLE_CUSTOMER:user_role:^/.*:GET|PUT|POST|PATCH|DELETE|OPTIONS",
            "ROLE_MERCHANT:user_role:^/.*:GET|PUT|POST|PATCH|DELETE|OPTIONS",
            "ROLE_READ:oauth_role:^/.*:GET|PUT|POST|PATCH|DELETE|OPTIONS",
            "ROLE_WRITE:oauth_role:^/.*:GET|PUT|POST|PATCH|DELETE|OPTIONS"
    };

    @Override
    @Transactional
    public void run(ApplicationArguments applicationArguments) {
        String password = encoder.encode(defaultPassword);
        this.insertRoles();
        this.insertClients(password);
        this.insertUser(password);
//        this.insertMerchant(password);
    }

    @Transactional
    public void insertRoles() {
        for (String role : roles) {
            String[] str = role.split(":");
            String name = str[0];
            String type = str[1];
            String pattern = str[2];
            String[] methods = str[3].split("\\|");
            Role oldRole = roleRepository.findOneByName(name);
            if (null == oldRole) {
                oldRole = new Role();
                oldRole.setId(UUID.randomUUID());  // Set the ID
                oldRole.setName(name);
                oldRole.setType(type);
                oldRole.setRolePaths(new ArrayList<>());
                for (String m : methods) {
                    String rolePathName = name.toLowerCase() + "_" + m.toLowerCase();
                    RolePath rolePath = rolePathRepository.findOneByName(rolePathName);
                    if (null == rolePath) {
                        rolePath = new RolePath();
                        rolePath.setId(UUID.randomUUID());  // Set the ID
                        rolePath.setName(rolePathName);
                        rolePath.setMethod(m.toUpperCase());
                        rolePath.setPattern(pattern);
                        rolePath.setRole(oldRole);
                        rolePathRepository.save(rolePath);
                        oldRole.getRolePaths().add(rolePath);
                    }
                }
            }
            roleRepository.save(oldRole);
        }
    }

    @Transactional
    public void insertClients(String password) {
        for (String c : clients) {
            String[] s = c.split(":");
            String clientName = s[0];
            String[] clientRoles = s[1].split("\\s");
            Client oldClient = clientRepository.findOneByClientId(clientName);
            if (null == oldClient) {
                oldClient = new Client();
                oldClient.setId(UUID.randomUUID());
                oldClient.setClientId(clientName);
                oldClient.setAccessTokenValiditySeconds(28800);
                oldClient.setRefreshTokenValiditySeconds(7257600);
                oldClient.setGrantTypes("password refresh_token authorization_code");
                oldClient.setClientSecret(password);
                oldClient.setApproved(true);
                oldClient.setRedirectUris("");
                oldClient.setScopes("read write");
                List<Role> rls = roleRepository.findByNameIn(clientRoles);
                if (rls.size() > 0) {
                    oldClient.getAuthorities().addAll(rls);
                }
            }
            clientRepository.save(oldClient);
        }
    }

    @Transactional
    public void insertUser(String password) {
        for (String userNames : users) {
            String[] str = userNames.split(":");
            String username = str[0];
            String[] roleNames = str[1].split("\\s");
            User oldUser = userRepository.findOneByUsername(username);
            if (null == oldUser) {
                oldUser = new User();
                oldUser.setId(UUID.randomUUID());
                oldUser.setUsername(username);
                oldUser.setPassword(password);
                List<Role> r = roleRepository.findByNameIn(roleNames);
                oldUser.setRoles(r);
            }
            userRepository.save(oldUser);
        }
    }

//    @Transactional
//    public void insertMerchant(String password) {
//        for (String merchantNames : merchants) {
//            String[] str = merchantNames.split(":");
//            String merchantName = str[0];
//            String[] roleNames = str[1].split("\\s");
//            Merchant oldMerchant = merchantRepository.findOneByMerchantName(merchantName);
//            if (null == oldMerchant) {
//                oldMerchant = new Merchant();
//                oldMerchant.setId(UUID.randomUUID());
//                oldMerchant.setUsername(merchantName);
//                oldMerchant.setPassword(password);
//                List<Role> r = roleRepository.findByNameIn(roleNames);
//                oldMerchant.setRoles(r);
//            }
//            merchantRepository.save(oldMerchant);
//        }
//    }
}