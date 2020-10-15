package com.store.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.store.validation.constraint.PasswordMatches;
import com.store.validation.constraint.ValidEmail;
import com.store.validation.constraint.ValidPassword;

import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@PasswordMatches
public class SignUpRequest {

    @NotBlank(message = "First name must not be blank")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    private String lastName;

    @NotBlank(message = "Username must not be blank")
    private String username;

    @ValidPassword
    private String password;

    @Transient
    @JsonProperty(value = "matchingPassword", access = JsonProperty.Access.WRITE_ONLY)
    private String matchingPassword;

    @ValidEmail
    private String email;

    private Set<String> role;

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

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
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

    public Set<String> getRole() {
        return role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
