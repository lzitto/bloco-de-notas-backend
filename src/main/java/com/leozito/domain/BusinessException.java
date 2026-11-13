package com.leozito.domain;

// extends RuntimeException = unchecked: quem chama não é obrigado a try/catch
// Escolha: regra pura deve ser testável sem JAX-RS
public class BusinessException extends RuntimeException {

    // construtor único: mensagem explica a regra quebrada (vai para o teste e para a API)
    public BusinessException(String message) {
        super(message); // guarda a mensagem no Throwable
    }
}