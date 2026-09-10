package com.leozito.resource;

import com.leozito.domain.User;
import com.leozito.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    
    @Inject
    UserService userService;

    @GET 
    public List<User> listarTodos() {
        return userService.listarTodos();
    }

    @GET 
    @Path("/{id}")
    public User buscarPorId(@PathParam("id") UUID id) {
            return userService.buscarPorId(id);
    }

    @POST
    public Response criar(User user) {
        User novoUsuario = userService.criar(user);
        return Response.status(Response.Status.CREATED).entity(novoUsuario).build();
    }

    
    @PUT
    @Path("/{id}")
    public User atualizar(@PathParam("id") UUID id, User user) {
        return userService.atualizar(id, user);
    }

    @DELETE 
    @Path("/{id}")
    public Response deletar(@PathParam("id") UUID id) {
        userService.deletar(id);
        return Response.noContent().build();
    }
}
