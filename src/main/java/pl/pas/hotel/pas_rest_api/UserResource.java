package pl.pas.hotel.pas_rest_api;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.pas.hotel.dto.user.AdminDto;
import pl.pas.hotel.dto.user.ClientDto;
import pl.pas.hotel.dto.user.ManagerDto;
import pl.pas.hotel.dto.user.mapper.AdminDtoMapper;
import pl.pas.hotel.dto.user.mapper.ClientDtoMapper;
import pl.pas.hotel.dto.user.mapper.ManagerDtoMapper;
import pl.pas.hotel.managers.UserManager;
import pl.pas.hotel.model.user.admin.Admin;
import pl.pas.hotel.model.user.client.Client;
import pl.pas.hotel.model.user.manager.Manager;

import java.util.UUID;

@Path("/users")
public class UserResource {

    @Inject
    UserManager userManager;

    @Inject
    AdminDtoMapper adminDtoMapper;

    @Inject
    ManagerDtoMapper managerDtoMapper;

    @Inject
    ClientDtoMapper clientDtoMapper;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/client")
    public Response createClient(@Valid ClientDto clientDto) {
        Client client = clientDtoMapper.toClient(clientDto);
        userManager.registerClient(client.getFirstName(), client.getLastName(), client.getPersonalId(), client.getAddress(), client.getLogin());
        return Response.ok().entity(client).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/admin")
    public Response createAdmin(@Valid AdminDto adminDto) {
        Admin admin = adminDtoMapper.toAdmin(adminDto);
        userManager.registerAdmin(admin.getLogin());
        return Response.ok().entity(admin).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/manager")
    public Response createManager(@Valid ManagerDto managerDto) {
        Manager manager = managerDtoMapper.toManager(managerDto);
        userManager.registerManager(manager.getLogin());
        return Response.ok().entity(manager).build();
    }

    @PUT
    @Path("/client/{uuid}")
    public Response updateClient(@PathParam("uuid") UUID roomId, @Valid ClientDto clientDto) {
        if(userManager.getUserById(roomId) == null ) {
            return Response.status(404).build();
        }
        Client client = clientDtoMapper.toClient(clientDto);
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

