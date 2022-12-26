package pl.pas.hotel.pas_rest_api;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.pas.hotel.dto.user.AdminDto;
import pl.pas.hotel.dto.user.ClientDto;
import pl.pas.hotel.dto.user.ManagerDto;
import pl.pas.hotel.dto.user.UserDto;
import pl.pas.hotel.dto.user.mapper.UserDtoMapper;
import pl.pas.hotel.exceptions.*;
import pl.pas.hotel.managers.UserManager;
import pl.pas.hotel.model.user.User;
import pl.pas.hotel.model.user.admin.Admin;
import pl.pas.hotel.model.user.client.Client;
import pl.pas.hotel.model.user.manager.Manager;

import java.util.UUID;

@RequestScoped
@Path("/users")
public class UserResource {

    @Inject
    UserManager userManager;

    @Inject
    UserDtoMapper userDtoMapper;

    @POST
    @Path("/client")
    public Response createClient(@Valid ClientDto clientDto) throws ClientValidationFailed {
        Client client = (Client) userDtoMapper.toUser(clientDto);
        userManager.registerClient(client.getFirstName(), client.getLastName(), client.getPersonalId(), client.getAddress(), client.getLogin());
        return Response.ok().entity(client).build();
    }

    @POST
    @Path("/admin")
    public Response createAdmin(@Valid AdminDto adminDto) throws AdminValidationFailed {
        Admin admin = (Admin) userDtoMapper.toUser(adminDto);
        userManager.registerAdmin(admin.getLogin());
        return Response.ok().entity(admin).build();
    }

    @POST
    @Path("/manager")
    public Response createManager(@Valid ManagerDto managerDto) throws ManagerValidationFailed {
        Manager manager = (Manager) userDtoMapper.toUser(managerDto);
        userManager.registerManager(manager.getLogin());
        return Response.ok().entity(manager).build();
    }

    @PUT
    @Path("/client/{uuid}")
    public Response updateClient(@PathParam("uuid") UUID id, @Valid ClientDto clientDto) throws UserWithGivenIdNotFound {
        if(userManager.getUserById(id) == null ) {
            return Response.status(404).build();
        }
        Client client = (Client) userDtoMapper.toUser(clientDto);
        userManager.updateUser(id, client.getFirstName(), client.getLastName(), client.getAddress(), client.getLogin());
        return Response.ok().build();
    }

    @PUT
    @Path("/admin/{uuid}")
    public Response updateAdmin(@PathParam("uuid") UUID id, @Valid AdminDto adminDto) throws UserWithGivenIdNotFound {
        if(userManager.getUserById(id) == null ) {
            return Response.status(404).build();
        }
        Admin admin = (Admin) userDtoMapper.toUser(adminDto);
        userManager.updateUser(id, null, null, null, admin.getLogin());
        return Response.ok().build();
    }

    @PUT
    @Path("/manager/{uuid}")
    public Response updateUser(@PathParam("uuid") UUID id, @Valid ManagerDto managerDto) throws UserWithGivenIdNotFound {
        if(userManager.getUserById(id) == null ) {
            return Response.status(404).build();
        }
        Manager manager = (Manager) userDtoMapper.toUser(managerDto);
        userManager.updateUser(id, null, null, null, manager.getLogin());
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        return Response.ok().entity(userManager.getAllUsers()).build();
    }

    @DELETE
    @Path("/{uuid}")
    public Response deleteUser(@PathParam("uuid")UUID userId) throws UserWithGivenIdNotFound {
        if(userManager.getUserById(userId) == null ) {
            return Response.status(404).build();
        }
        userManager.deleteUser(userId);
        return Response.ok().build();
    }

    @GET
    @Path("/{uuid}")
    public Response getUser(@PathParam("uuid") UUID userId) throws UserWithGivenIdNotFound {
        if(userManager.getUserById(userId) == null ) {
            return Response.status(404).build();
        }
        return Response.ok().entity(userManager.getUserById(userId)).build();
    }

}

