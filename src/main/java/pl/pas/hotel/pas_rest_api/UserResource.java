package pl.pas.hotel.pas_rest_api;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.pas.hotel.managers.UserManager;
import pl.pas.hotel.model.user.admin.Admin;
import pl.pas.hotel.model.user.client.Client;
import pl.pas.hotel.model.user.manager.Manager;

import java.util.UUID;

@Path("/users")
public class UserResource {

    @Inject
    UserManager userManager;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/client")
    public Response createClient(@Valid Client client) {
        userManager.registerClient(client.getFirstName(), client.getLastName(), client.getPersonalId(), client.getAddress(), client.getLogin());
        return Response.ok().entity(client).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/admin")
    public Response createAdmin(@Valid Admin admin) {
        userManager.registerAdmin(admin.getLogin());
        return Response.ok().entity(admin).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/manager")
    public Response createManager(@Valid Manager manager) {
        userManager.registerManager(manager.getLogin());
        return Response.ok().entity(manager).build();
    }

    @PUT
    @Path("/client/{uuid}")
    public Response updateClient(@PathParam("uuid") UUID roomId, @Valid Client client) {
        if(userManager.getUserById(roomId) == null ) {
            return Response.status(404).build();
        }
        userManager.updateClient(roomId, client.getFirstName(), client.getLastName(), client.getPersonalId(), client.getAddress(), client.getLogin());
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        return Response.ok().entity(userManager.getAllUsers()).build();
    }

    @DELETE
    @Path("/{uuid}")
    public Response deleteUser(@PathParam("uuid")UUID userId) {
        if(userManager.getUserById(userId) == null ) {
            return Response.status(404).build();
        }
        userManager.deleteUser(userId);
        return Response.ok().build();
    }

    @GET
    @Path("/{uuid}")
    public Response getUser(@PathParam("uuid") UUID userId) {
        if(userManager.getUserById(userId) == null ) {
            return Response.status(404).build();
        }
        return Response.ok().entity(userManager.getUserById(userId)).build();
    }

}

