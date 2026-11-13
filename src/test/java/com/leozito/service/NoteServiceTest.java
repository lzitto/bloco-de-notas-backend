// Pacote do serviço testado — teste unitário fica perto da classe (convenção)
package com.leozito.service;

import com.leozito.domain.Note;                 // entidade que o service manipula
import jakarta.ws.rs.NotFoundException;         // exceção que o NoteService LANÇA (NoteService.java:26)
import org.junit.jupiter.api.BeforeEach;        // roda antes de CADA @Test
import org.junit.jupiter.api.Test;              // marca método como teste JUnit 5

import java.util.List;                          // retorno de buscarPorUsuario
import java.util.UUID;                          // ids das notas/usuários

import static org.junit.jupiter.api.Assertions.*; // assertThrows, assertEquals, assertNotNull...

// SEM @QuarkusTest de propósito: só JVM + JUnit = teste UNITÁRIO (rápido, isola a regra)
class NoteServiceTest {

    private NoteService noteService;            // referência criada em cada teste

    @BeforeEach                                 // por que não static/um só para todos?
    void setUp() {                              // → o Map fica na INSTÂNCIA; serviço novo
        noteService = new NoteService();        //   = estado zerado = testes não poluem uns aos outros
    }

    @Test
    void criarDeveGerarIdEPermitirBusca() {
        Note nota = new Note(UUID.randomUUID(), "T1", "C1");  // dado de entrada
        Note criada = noteService.criar(nota);                 // quando: chama o método real
        assertNotNull(criada.getId());                         // então: UUID foi gerado (não null)
        assertEquals(criada, noteService.buscarPorId(criada.getId())); // e achou de volta no Map
    }

    @Test
    void buscarPorIdInexistenteDeveLancarNotFound() {
        UUID idQueNaoExiste = UUID.randomUUID();              // id nunca inserido no Map
        // assertThrows(EXCECAÇÃO_ESPERADA, lambda) = "deve lançar X ao executar Y"
        assertThrows(NotFoundException.class,                 // era essa a previsão da Q1: (b)
                () -> noteService.buscarPorId(idQueNaoExiste)); // lambda = código que deve falhar
    }

    @Test
    void buscarPorUsuarioDeveFiltrarApenasNotasDesteUsuario() {
        UUID dono1 = UUID.randomUUID();                       // dois donos diferentes
        UUID dono2 = UUID.randomUUID();
        noteService.criar(new Note(dono1, "A", "x"));         // 2 notas do dono1
        noteService.criar(new Note(dono1, "B", "y"));
        noteService.criar(new Note(dono2, "C", "z"));         // 1 nota do dono2

        List<Note> doDono1 = noteService.buscarPorUsuario(dono1);

        assertEquals(2, doDono1.size());                      // filtrou certo (2, não 3)
        assertTrue(doDono1.stream()                           // e NENHUMA é de outro dono
                .allMatch(n -> dono1.equals(n.getUserId()))); // allMatch = predicado p/ todos
    }

    @Test
    void atualizarDeveTrocarTituloEContent() {
        UUID dono = UUID.randomUUID();
        Note original = noteService.criar(new Note(dono, "Velho", "Antigo"));
        Note novaVersao = new Note(dono, "Novo", "Moderno");

        noteService.atualizar(original.getId(), novaVersao);  // regra do service: copia title/content

        Note depois = noteService.buscarPorId(original.getId());
        assertEquals("Novo", depois.getTitle());              // título trocou
        assertEquals("Moderno", depois.getContent());         // conteúdo trocou
    }

    @Test
    void deletarDeveRemoverEProximaBuscaFalhar() {
        UUID dono = UUID.randomUUID();
        Note nota = noteService.criar(new Note(dono, "T", "C"));

        noteService.deletar(nota.getId());                    // remove do Map

        assertThrows(NotFoundException.class,                 // não achou mais = removida
                () -> noteService.buscarPorId(nota.getId()));
    }
}