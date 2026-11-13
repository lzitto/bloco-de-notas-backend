package com.leozito.resource;

import com.leozito.domain.User;
import com.leozito.dto.UserRequest;
import com.leozito.service.UserService;
import org.eclipse.microprofile.openapi.annotations.Operation; // anotação MicroProfile (vem com smallrye-openapi)
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.validation.Valid;              // dispara as anotações do DTO
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Tag(name = "Usuários", description = "CRUD de usuários") // Swagger: agrupa no UI
@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService; // CDI injeta o bean @ApplicationScoped

    @GET
    @Operation(summary = "Lista todos os usuários") // Swagger: descrição do endpoint
    public List<User> listarTodos() {
        return userService.listarTodos();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Busca usuário por ID")
    public User buscarPorId(@PathParam("id") UUID id) {
        return userService.buscarPorId(id);
    }

    @POST
    @Operation(summary = "Cria usuário (email único)")
    public Response criar(@Valid UserRequest dto) { // @Valid = roda @NotBlank/@Email
        // mapeia DTO → domínio: regras do service continuam valendo
        User user = new User(dto.getEmail(), dto.getPassword());
        User novoUsuario = userService.criar(user);
        return Response.status(Response.Status.CREATED).entity(novoUsuario).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualiza usuário")
    public User atualizar(@PathParam("id") UUID id, @Valid UserRequest dto) {
        User dados = new User(dto.getEmail(), dto.getPassword());
        return userService.atualizar(id, dados);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Remove usuário")
    public Response deletar(@PathParam("id") UUID id) {
        userService.deletar(id);
        return Response.noContent().build();
    }
}