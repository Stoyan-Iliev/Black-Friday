package com.store.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.store.validation.constraint.PasswordMatches;
import com.store.validation.constraint.ValidEmail;
import com.store.validation.constraint.ValidPassword;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity(name = "users")
@PasswordMatches
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @NotBlank(message = "First name must not be blank")
    private String firstName;

    @Column
    @NotBlank(message = "Last name must not be blank")
    private String lastName;

    @Column
    @NotBlank(message = "Username must not be blank")
    private String username;

    @Column
    @ValidPassword
    private String password;

    @Transient
    @JsonProperty(value = "matchingPassword", access = JsonProperty.Access.WRITE_ONLY)
    private String matchingPassword;

    @Column
    @ValidEmail
    private String email;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> role;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }
}
