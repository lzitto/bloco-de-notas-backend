package com.leozito.service;

import com.leozito.domain.BusinessException;
import com.leozito.domain.Note;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@ApplicationScoped
public class NoteService {

    private final Map<UUID, Note> noteDatabase = new ConcurrentHashMap<>();

    private final UserService userService;

    @Inject
    public NoteService(UserService userService) {
        this.userService = userService;
    }

    public List<Note> listarTodas() {
        return new ArrayList<>(noteDatabase.values());
    }

    public Note buscarPorId(UUID id) {
        Note note = noteDatabase.get(id);
        if (note == null) {
            throw new NotFoundException("Nota com o ID " + id + " não foi encontrada.");
        }
        return note;
    }

    public List<Note> buscarPorUsuario(UUID userId) {
        return noteDatabase.values().stream()
                .filter(note -> userId.equals(note.getUserId()))
                .collect(Collectors.toList());
    }

    public Note criar(Note note) {
        if (!userService.existe(note.getUserId())) {
            throw new BusinessException("Usuário não existe: " + note.getUserId());
        }
        if (note.getId() == null) {
            note.setId(UUID.randomUUID());
        }
        noteDatabase.put(note.getId(), note);
        return note;
    }

    public Note atualizar(UUID id, Note noteAtualizada) {
        Note noteExistente = buscarPorId(id);

        noteExistente.setTitle(noteAtualizada.getTitle());
        noteExistente.setContent(noteAtualizada.getContent());

        noteDatabase.put(id, noteExistente);
        return noteExistente;
    }

    public void deletar(UUID id) {
        buscarPorId(id);
        noteDatabase.remove(id);
    }
}