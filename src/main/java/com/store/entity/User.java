package com.store.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.store.validation.constraint.PasswordMatches;
import com.store.validation.constraint.ValidPassword;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity(name = "users")
@PasswordMatches
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @NotNull
    @NotBlank
    private String firstName;

    @Column
    @NotNull
    @NotBlank
    private String lastName;

    @Column
    @NotNull
    @NotBlank
    private String username;

    @Column
    @NotNull
    @ValidPassword
    private String password;

    @Transient
    @JsonProperty(value = "matchingPassword", access = JsonProperty.Access.WRITE_ONLY)
    private String matchingPassword;

    @Column
    @NotNull
    @Email
    private String email;

    @Column
    private boolean isEmployee;

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

    public boolean isEmployee() {
        return isEmployee;
    }

    public void setEmployee(boolean employee) {
        isEmployee = employee;
    }
}
