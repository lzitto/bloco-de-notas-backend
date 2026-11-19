package com.leozito.service;

import com.leozito.domain.BusinessException;
import com.leozito.domain.User;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void criarDeveGuardarUsuarioComSenhaHasheada() {
        User user = new User("ana@mail.com", "senha123");
        User criado = userService.criar(user);

        assertNotNull(criado.getId());
        assertEquals("ana@mail.com", criado.getEmail());
        assertTrue(BcryptUtil.matches("senha123", criado.getPassword()));
        assertNotEquals("senha123", criado.getPassword());
    }

    @Test
    void listarTodosDeveContarOServicosCriados() {
        userService.criar(new User("a@x.com", "1"));
        userService.criar(new User("b@x.com", "2"));

        List<User> todos = userService.listarTodos();

        assertEquals(2, todos.size());
    }

    @Test
    void buscarPorIdInexistenteDeveLancarNotFound() {
        UUID idDesconhecido = UUID.randomUUID();
        assertThrows(NotFoundException.class,
                () -> userService.buscarPorId(idDesconhecido));
    }

    @Test
    void atualizarDeveTrocarEmailEPassword() {
        User original = userService.criar(new User("old@x.com", "antiga"));
        User dadosNovos = new User("new@x.com", "nova");

        userService.atualizar(original.getId(), dadosNovos);

        User depois = userService.buscarPorId(original.getId());
        assertEquals("new@x.com", depois.getEmail());
        assertTrue(BcryptUtil.matches("nova", depois.getPassword()));
        assertNotEquals("nova", depois.getPassword());
    }

    @Test
    void deletarDeveRemoverUsuario() {
        User user = userService.criar(new User("z@x.com", "s"));
        userService.deletar(user.getId());

        assertThrows(NotFoundException.class,
                () -> userService.buscarPorId(user.getId()));
    }

    @Test
    void criarComEmailDuplicadoDeveLancarBusinessException() {
        userService.criar(new User("dup@x.com", "senha1"));
        assertThrows(BusinessException.class,
                () -> userService.criar(new User("dup@x.com", "senha2")));
    }

    @Test
    void atualizarComEmailDeOutroUsuarioDeveLancarBusinessException() {
        userService.criar(new User("a@x.com", "1"));
        User segundo = userService.criar(new User("b@x.com", "2"));

        assertThrows(BusinessException.class,
                () -> userService.atualizar(segundo.getId(), new User("a@x.com", "3")));
    }

    @Test
    void atualizarUsuarioInexistenteDeveLancarNotFound() {
        UUID idDesconhecido = UUID.randomUUID();

        assertThrows(NotFoundException.class,
                () -> userService.atualizar(idDesconhecido, new User("x@x.com", "1")));
    }

    @Test
    void deletarUsuarioInexistenteDeveLancarNotFound() {
        UUID idDesconhecido = UUID.randomUUID();

        assertThrows(NotFoundException.class,
                () -> userService.deletar(idDesconhecido));
    }
}