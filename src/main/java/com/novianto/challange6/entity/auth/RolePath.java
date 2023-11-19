package com.novianto.challange6.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "role_path")
public class RolePath implements Serializable {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;
    @Column(length = 50)
    private String name;
    private String pattern;
    private String method;
    @ManyToOne(targetEntity = Role.class, cascade = CascadeType.ALL)
    @JsonIgnore
    private Role role;

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

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
