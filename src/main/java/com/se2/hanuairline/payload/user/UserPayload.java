package com.se2.hanuairline.payload.user;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.se2.hanuairline.model.user.AuthProvider;
import com.se2.hanuairline.model.user.RoleName;
import com.se2.hanuairline.model.user.UserStatus;
import org.hibernate.annotations.NaturalId;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserPayload {
    // userpayload thieu
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
    @NotNull
    private String imageUrl;
    @NotNull
    private String password;
    @NotNull
    private AuthProvider provider;
    @NotNull
    private String providerId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserStatus status;

        

    @Enumerated(EnumType.STRING)
//    @JsonEnumDefaultValue("${RoleName.ROLE_USER}")
    private RoleName role=RoleName.ROLE_USER;


//    public UserPayload( @NotBlank @Size(max = 40) String name, @NotBlank @Size(max = 15) String username, @NotBlank @Size(max = 40) @Email String email) {
////        System.out.println("CheckRoleName"+roleName);
//        this.name = name;
//        this.username = username;
//        this.email = email;
////        this.role = roleName;
//    }

    public UserPayload( String name, String username, String email, String imageUrl, String password, AuthProvider provider, String providerId, UserStatus status, RoleName role) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.imageUrl = imageUrl;
        this.password = password;
        this.provider = provider;
        this.providerId = providerId;
        this.status = status;
        this.role = role;
    }

    public UserPayload(String name, String username, String email, String imageUrl, String password, AuthProvider provider, String providerId, UserStatus status) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.imageUrl = imageUrl;
        this.password = password;
        this.provider = provider;
        this.providerId = providerId;
        this.status = status;
    }

    public UserPayload(String name, String username, String email, String imageUrl, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.imageUrl = imageUrl;
        this.password = password;
        System.out.println("PASS IN USER PAYLOAD : "+this.password);
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

    public RoleName getRole() {
        return role;
    }

    public void setRole(RoleName role) {
        this.role = role;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
