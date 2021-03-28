package com.se2.hanuairline.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.se2.hanuairline.model.Ticket;
import com.se2.hanuairline.model.audit.DateAudit;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "User", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
public class User extends DateAudit implements Cloneable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank
        @Size(max = 40)
        private String name;

        @NaturalId
        @NotBlank
        @Size(max = 15)
        private String username;

        @NaturalId
        @NotBlank
        @Size(max = 40)
        @Email
        private String email;

        private String imageUrl;

        @NotBlank
        @Size(max = 100)
        private String password;

        @NotNull
        @Enumerated(EnumType.STRING)
        private AuthProvider provider;

        private String providerId;

        @NotNull
        @Enumerated(EnumType.STRING)
        private UserStatus status;

        @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
        @JoinTable(name = "User_role",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
        private Set<Role> roles = new HashSet<>();

        @OneToMany(mappedBy = "user")
        private Set<Ticket> ticket;

        @OneToOne(mappedBy = "user",cascade = CascadeType.REMOVE)
        private Profile profile;

        public User(Long id, @NotBlank @Size(max = 40) String name, @NotBlank @Size(max = 15) String username, @NotBlank @Size(max = 40) @Email String email, String imageUrl, @NotBlank @Size(max = 100) String password, @NotNull AuthProvider provider, String providerId, UserStatus status) {
                this.id = id;
                this.name = name;
                this.username = username;
                this.email = email;
                this.imageUrl = imageUrl;
                this.password = password;
                this.provider = provider;
                this.providerId = providerId;
                this.status = status;
        }

        public User(){}

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public Set<Role> getRoles() {
                return roles;
        }

        public void setRoles(Set<Role> roles) {
                this.roles = roles;
        }

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public String getImageUrl() {
                return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
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
