// Pacote do serviço testado — teste unitário fica perto da classe (convenção)
package com.leozito.service;

import com.leozito.domain.BusinessException;
import com.leozito.domain.Note;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// SEM @QuarkusTest de propósito: só JVM + JUnit = teste UNITÁRIO (rápido, isola a regra)
class NoteServiceTest {

    private NoteService noteService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        when(userService.existe(any(UUID.class))).thenReturn(true);
        noteService = new NoteService(userService);
    }

    @Test
    void criarDeveGuardarNotaEPermitirBuscaPorId() {
        Note nota = new Note(UUID.randomUUID(), "T1", "C1");
        Note criada = noteService.criar(nota);
        assertEquals(criada, noteService.buscarPorId(criada.getId()));
    }

    @Test
    void buscarPorIdInexistenteDeveLancarNotFound() {
        UUID idQueNaoExiste = UUID.randomUUID();
        assertThrows(NotFoundException.class,
                () -> noteService.buscarPorId(idQueNaoExiste));
    }

    @Test
    void buscarPorUsuarioDeveFiltrarApenasNotasDesteUsuario() {
        UUID dono1 = UUID.randomUUID();
        UUID dono2 = UUID.randomUUID();
        noteService.criar(new Note(dono1, "A", "x"));
        noteService.criar(new Note(dono1, "B", "y"));
        noteService.criar(new Note(dono2, "C", "z"));

        List<Note> doDono1 = noteService.buscarPorUsuario(dono1);

        assertEquals(2, doDono1.size());
        assertTrue(doDono1.stream()
                .allMatch(n -> dono1.equals(n.getUserId())));
    }

    @Test
    void atualizarDeveTrocarTituloEContent() {
        UUID dono = UUID.randomUUID();
        Note original = noteService.criar(new Note(dono, "Velho", "Antigo"));
        Note novaVersao = new Note(dono, "Novo", "Moderno");

        noteService.atualizar(original.getId(), novaVersao);

        Note depois = noteService.buscarPorId(original.getId());
        assertEquals("Novo", depois.getTitle());
        assertEquals("Moderno", depois.getContent());
    }

    @Test
    void deletarDeveRemoverEProximaBuscaFalhar() {
        UUID dono = UUID.randomUUID();
        Note nota = noteService.criar(new Note(dono, "T", "C"));

        noteService.deletar(nota.getId());

        assertThrows(NotFoundException.class,
                () -> noteService.buscarPorId(nota.getId()));
    }

    @Test
    void criarComUsuarioInexistenteDeveLancarBusinessException() {
        UUID dono = UUID.randomUUID();
        when(userService.existe(dono)).thenReturn(false);

        assertThrows(BusinessException.class,
                () -> noteService.criar(new Note(dono, "T", "C")));

        assertTrue(noteService.listarTodas().isEmpty());
        verify(userService).existe(dono);
    }

    @Test
    void listarTodasDeveRetornarNotasCadastradas() {
        noteService.criar(new Note(UUID.randomUUID(), "A", "1"));
        noteService.criar(new Note(UUID.randomUUID(), "B", "2"));

        assertEquals(2, noteService.listarTodas().size());
    }

    @Test
    void atualizarNotaInexistenteDeveLancarNotFound() {
        UUID idQueNaoExiste = UUID.randomUUID();

        assertThrows(NotFoundException.class,
                () -> noteService.atualizar(idQueNaoExiste,
                        new Note(UUID.randomUUID(), "T", "C")));
    }

    @Test
    void deletarNotaInexistenteDeveLancarNotFound() {
        UUID idQueNaoExiste = UUID.randomUUID();

        assertThrows(NotFoundException.class,
                () -> noteService.deletar(idQueNaoExiste));
    }

    @Test
    void buscarPorUsuarioSemNotasDeveRetornarListaVazia() {
        UUID donoSemNotas = UUID.randomUUID();

        assertTrue(noteService.buscarPorUsuario(donoSemNotas).isEmpty());
    }
}