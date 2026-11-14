// DTO de ENTRADA: só o que o cliente pode enviar (sem id, sem campos internos)
package com.leozito.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// validação DECLARATIVA: quem lê a classe já vê as regras de formato
public class UserRequest {

    @NotBlank(message = "email é obrigatório")   // null, "" ou "   " → inválido
    @Email(message = "email deve ter formato válido") // ex: sem "@"
    private String email;

    @NotBlank(message = "password é obrigatória")
    private String password;

    // getters e setters: Jackson desserializa JSON → objeto por eles
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}