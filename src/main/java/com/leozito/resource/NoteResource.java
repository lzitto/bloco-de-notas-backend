package com.leozito.resource;

import com.leozito.domain.Note;
import com.leozito.dto.NoteRequest;
import com.leozito.service.NoteService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Tag(name = "Notas", description = "CRUD de notas do usuário") // Agrupa no Swagger UI
@Path("/api/notes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NoteResource {

    @Inject
    NoteService noteService;

    @GET
    @Operation(summary = "Lista todas as notas")
    public List<Note> listarTodas() {
        return noteService.listarTodas();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Busca nota por ID")
    public Note buscarPorId(@PathParam("id") UUID id) {
        return noteService.buscarPorId(id);
    }

    @GET
    @Path("/user/{userId}")
    @Operation(summary = "Busca notas por usuário")
    public List<Note> buscarPorUsuario(@PathParam("userId") UUID userId) {
        return noteService.buscarPorUsuario(userId);
    }

    @POST
    @Operation(summary = "Cria nota")
    public Response criar(@Valid NoteRequest dto) {
        Note note = new Note(dto.getUserId(), dto.getTitle(), dto.getContent());
        Note novaNota = noteService.criar(note);
        return Response.status(Response.Status.CREATED).entity(novaNota).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualiza nota")
    public Note atualizar(@PathParam("id") UUID id, @Valid NoteRequest dto) {
        Note dados = new Note(dto.getUserId(), dto.getTitle(), dto.getContent());
        return noteService.atualizar(id, dados);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Remove nota")
    public Response deletar(@PathParam("id") UUID id) {
        noteService.deletar(id);
        return Response.noContent().build();
    }
}