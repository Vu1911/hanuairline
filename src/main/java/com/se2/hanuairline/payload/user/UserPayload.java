package com.se2.hanuairline.payload.user;

import com.se2.hanuairline.model.user.AuthProvider;
import com.se2.hanuairline.model.user.UserStatus;
import org.hibernate.annotations.NaturalId;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserPayload {
    @Id
    Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @NotBlank
    @Size(max = 15)
    private String username;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    public UserPayload(Long id, @NotBlank @Size(max = 40) String name, @NotBlank @Size(max = 15) String username, @NotBlank @Size(max = 40) @Email String email) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
