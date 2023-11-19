package com.novianto.challange6.service.auth;

import com.novianto.challange6.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

@Service
public class Oauth2ClientDetailsService implements ClientDetailsService {
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public ClientDetails loadClientByClientId(String s) throws ClientRegistrationException {
        ClientDetails client = clientRepository.findOneByClientId(s);
        if (null == client) {
            throw new ClientRegistrationException("Client not found");
        }
        return client;
    }

    @CacheEvict("client_id")
    public void clearCache(String s) {
        System.out.println("ini cache client_id = " + s);
    }
}
