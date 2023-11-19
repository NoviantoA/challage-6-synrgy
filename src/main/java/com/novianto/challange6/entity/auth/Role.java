package com.novianto.challange6.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.novianto.challange6.entity.User;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "role", uniqueConstraints = {
        @UniqueConstraint(name = "role_name_and_type", columnNames = {"type", "name"})
})
public class Role implements GrantedAuthority {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;
    @Column(length = 20)
    private String name;

    private String type;
    @JsonIgnore
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RolePath> rolePaths;

    @JsonIgnore
    @ManyToMany(targetEntity = User.class, mappedBy = "roles",fetch = FetchType.LAZY)
    private List<User> users;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    @JsonIgnore
    public String getAuthority() {
        return this.name;
    }

    public List<RolePath> getRolePaths() {
        return rolePaths;
    }

    public void setRolePaths(List<RolePath> rolePaths) {
        this.rolePaths = rolePaths;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
