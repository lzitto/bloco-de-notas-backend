package com.leozito.resource;

import com.leozito.domain.Note;
import com.leozito.service.NoteService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/api/notes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NoteResource {

    @Inject
    NoteService noteService;

    @GET
    public List<Note> listarTodas() {
        return noteService.listarTodas();
    }

    @GET
    @Path("/{id}")
    public Note buscarPorId(@PathParam("id") UUID id) {
        return noteService.buscarPorId(id);
    }

    @GET
    @Path("/user/{userId}")
    public List<Note> buscarPorUsuario(@PathParam("userId") UUID userId) {
        return noteService.buscarPorUsuario(userId);
    }

    @POST
    public Response criar(Note note) {
        Note novaNota = noteService.criar(note);
        return Response.status(Response.Status.CREATED).entity(novaNota).build();
    }

    @PUT
    @Path("/{id}")
    public Note atualizar(@PathParam("id") UUID id, Note note) {
        return noteService.atualizar(id, note);
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") UUID id) {
        noteService.deletar(id);
        return Response.noContent().build();
    }
}