package com.leozito.service;

import com.leozito.domain.BusinessException;
import com.leozito.domain.User;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class UserService {

    // simulando um banco de dados em memória usando um Map thread-safe
    private final Map<UUID, User> userDatabase = new ConcurrentHashMap<>();

    public List<User> listarTodos() {
        return new ArrayList<>(userDatabase.values());
    }

    public boolean existe(UUID id) {
        return id != null && userDatabase.containsKey(id);
    }

    public User buscarPorId(UUID id) {
        User user = userDatabase.get(id);
        if (user == null) {
            throw new NotFoundException("Usuário com ID " + id +" não foi encontrado.");
        }
        return user;
    }

    public User criar(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }

        validarEmailUnico(user.getEmail(), user.getId());
        user.setPassword(BcryptUtil.bcryptHash(user.getPassword()));

        userDatabase.put(user.getId(), user);
        return user;
    }

    public User atualizar(UUID id, User userAtualizado) {
        User userExistente = buscarPorId(id);

        validarEmailUnico(userAtualizado.getEmail(), id);

        userExistente.setEmail(userAtualizado.getEmail());
        userExistente.setPassword(BcryptUtil.bcryptHash(userAtualizado.getPassword()));

        userDatabase.put(id, userExistente);
        return userExistente;
    }

    private void validarEmailUnico(String email, UUID idIgnorado) {
        boolean jaExiste = userDatabase.values().stream()
                .filter(u -> !u.getId().equals(idIgnorado))   // ignora o próprio usuário
                .anyMatch(u -> Objects.equals(u.getEmail(), email));
        if (jaExiste) {
            throw new BusinessException("Email já cadastrado: " + email);
        }
    }
  
    public void deletar(UUID id) {
        buscarPorId(id); 
        userDatabase.remove(id);
    }

}