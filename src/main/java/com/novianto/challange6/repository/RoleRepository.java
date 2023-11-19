package com.novianto.challange6.repository;

import com.novianto.challange6.entity.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Role findOneByName(String name);
    List<Role> findByNameIn(String[] names);
}
