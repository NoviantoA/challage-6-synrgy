package com.novianto.challange6.service.auth;

import com.novianto.challange6.entity.User;
import com.novianto.challange6.repository.MerchantRepository;
import com.novianto.challange6.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class Oauth2UserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MerchantRepository merchantRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findOneByUsername(usernameOrEmail);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("Username or Email %s is not found", usernameOrEmail));
        }
        return user;
    }

    @CacheEvict("oauth_username")
    public void clearCache(String s) {
    }
}
