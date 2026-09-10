package com.leozito.service;

import com.leozito.domain.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class UserService {

    // simulando um banco de dados em memória usando um Map thread-safe
    private final Map<UUID, User> userDatabase = new ConcurrentHashMap<>();

    public List<User> listarTodos() {
        return new ArrayList<>(userDatabase.values());
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
        userDatabase.put(user.getId(), user);
        return user;
    }

    public User atualizar(UUID id, User userAtualizado) {
        User userExistente = buscarPorId(id);

        userExistente.setEmail(userAtualizado.getEmail());
        userExistente.setPassword(userAtualizado.getPassword());

        userDatabase.put(id, userExistente);
        return userExistente;

    }
  
    public void deletar(UUID id) {
        buscarPorId(id); 
        userDatabase.remove(id);
    }

}