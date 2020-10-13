package com.store.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @NotNull
    @Size(min = 3, max = 255)
    private String firstName;

    @Column
    @NotNull
    @Size(min = 3, max = 255)
    private String lastName;

    @Column
    @NotNull
    @Size(min = 3, max = 255)
    private String username;

    @Column
    @NotNull
    @Size(min = 6, max = 35)
    private String password;

    @Column
    @NotNull
    @Email
    private String email;

    @Column
    @NotNull
    private boolean isClient;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isClient() {
        return isClient;
    }

    public void setClient(boolean client) {
        isClient = client;
    }
}
