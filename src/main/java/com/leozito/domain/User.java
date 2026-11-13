package com.leozito.domain;
import com.fasterxml.jackson.annotation.JsonIgnore; // import novo

import java.util.UUID;

public class User {
    private UUID id;
    private String email;
    private String password;

    public User() {
        this.id = UUID.randomUUID();
    }

    public User(String email, String password) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore // Jackson: NÃO serializa a senha em nenhuma resposta JSON
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}