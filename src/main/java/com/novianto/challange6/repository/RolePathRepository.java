package com.novianto.challange6.repository;

import com.novianto.challange6.entity.auth.RolePath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

public interface RolePathRepository extends JpaRepository<RolePath, UUID> {
    RolePath findOneByName(String rolePathName);
    @Query(value = "SELECT p.* FROM role_path p " +
            "JOIN role r ON r.id = p.role_id " +
            "JOIN user_role ur ON ur.role_id = r.id " +
            "JOIN merchant_role mr ON mr.role_id = r.id " +
            "WHERE ur.user_id AND mr.merchant_id = ?1", nativeQuery = true)
    <T extends UserDetails> List<RolePath> findByUser(T user);
}
