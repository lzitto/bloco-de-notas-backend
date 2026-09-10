package com.leozito.service;

import java.util.List;

import com.leozito.domain.User;

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

        userExistente.setName(userAtualizado.getName());
        userExistente.setEmail(userAtualizado.getEmail());

        userDatabase.put(id, userExistente);
        return userExistente;

    }
    

    // finalziar o crud com o metodo delet


}