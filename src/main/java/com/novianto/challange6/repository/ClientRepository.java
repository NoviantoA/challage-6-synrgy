package com.novianto.challange6.repository;

import com.novianto.challange6.entity.auth.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
    Client findOneByClientId(String clientId);
}
