package com.leozito.service;

import com.leozito.domain.User;                 // entidade testada
import jakarta.ws.rs.NotFoundException;         // mesma exceção do UserService.buscarPorId
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.leozito.domain.BusinessException; 

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

// Mesma estrutura do NoteServiceTest: unitário puro, sem Quarkus
class UserServiceTest {

    private UserService userService;

    @BeforeEach                                  // serviço novo a cada teste = Map limpo
    void setUp() {
        userService = new UserService();
    }

    @Test
    void criarDeveGuardarUsuario() {
        User user = new User("ana@mail.com", "senha123");     // construtor gera o UUID (User.java:14-17)
        User criado = userService.criar(user);

        assertNotNull(criado.getId());
        assertEquals("ana@mail.com", criado.getEmail());      // sobreviveu ao put no Map
    }

    @Test
    void listarTodosDeveContarOServicosCriados() {
        userService.criar(new User("a@x.com", "1"));
        userService.criar(new User("b@x.com", "2"));

        List<User> todos = userService.listarTodos();

        assertEquals(2, todos.size());                        // 2 puts = 2 valores no Map
    }

    @Test
    void buscarPorIdInexistenteDeveLancarNotFound() {
        UUID idDesconhecido = UUID.randomUUID();
        assertThrows(NotFoundException.class,                 // espelha a regra do NoteService
                () -> userService.buscarPorId(idDesconhecido));
    }

    @Test
    void atualizarDeveTrocarEmailEPassword() {
        User original = userService.criar(new User("old@x.com", "antiga"));
        User dadosNovos = new User("new@x.com", "nova");

        userService.atualizar(original.getId(), dadosNovos);  // service copia email + password (UserService.java:42-43)

        User depois = userService.buscarPorId(original.getId());
        assertEquals("new@x.com", depois.getEmail());
        assertEquals("nova", depois.getPassword());
    }

    @Test
    void deletarDeveRemoverUsuario() {
        User user = userService.criar(new User("z@x.com", "s"));
        userService.deletar(user.getId());

        assertThrows(NotFoundException.class,
                () -> userService.buscarPorId(user.getId())); // sumiu do Map
    }

    @Test
    void criarComEmailDuplicadoDeveLancarBusinessException() {
        userService.criar(new User("dup@x.com", "senha1"));      // 1º ok
        // 2º mesmo email → deve falhar na regra
        assertThrows(BusinessException.class,
                () -> userService.criar(new User("dup@x.com", "senha2")));
    }
}