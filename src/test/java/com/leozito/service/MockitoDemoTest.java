package com.leozito.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*; // mock(), when(), verify()

// Sintaxe do Mockito SEM tocar no código de produção:
// quando UserService ganhar um colaborador (ex.: repositório), é exatamente este formato.
class MockitoDemoTest {

    // Colaborador fictício — interface aninhada só para o demo caber no teste
    interface Relogio {
        String agora();
    }

    @Test
    void mockSubstituiColaboradorEDevolveRespostaProgramada() {
        Relogio relogio = mock(Relogio.class);            // 1) cria falso: métodos não fazem nada ainda
        when(relogio.agora()).thenReturn("2026-11-13T00:00:00Z"); // 2) programa: "SE chamar agora(), DEVOLVE isto"

        String resultado = relogio.agora();               // 3) chama o mock (não o código real)

        assertEquals("2026-11-13T00:00:00Z", resultado);  // 4) devolveu o que programamos
        verify(relogio).agora();                          // 5) prova: o método agora() FOI chamado 1x
    }
}