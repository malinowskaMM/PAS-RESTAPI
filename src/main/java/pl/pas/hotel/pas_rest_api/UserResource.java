package pl.pas.hotel.pas_rest_api;

import com.nimbusds.jose.JOSEException;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.pas.hotel.dto.auth.PasswordChangeDto;
import pl.pas.hotel.dto.user.AdminDto;
import pl.pas.hotel.dto.user.ClientDto;
import pl.pas.hotel.dto.user.ManagerDto;
import pl.pas.hotel.dto.user.mapper.UserDtoMapper;
import pl.pas.hotel.exceptions.*;
import pl.pas.hotel.managers.UserManager;
import pl.pas.hotel.model.user.admin.Admin;
import pl.pas.hotel.model.user.client.Client;
import pl.pas.hotel.model.user.manager.Manager;

import java.text.ParseException;
import java.util.UUID;

@Path("/users")
public class UserResource {

    @Inject
    UserManager userManager;

    @Inject
    UserDtoMapper userDtoMapper;

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/passwordChange")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    public Response changeUserPassword(@NotNull PasswordChangeDto passwordChangeDto) {
        if (passwordChangeDto.getNewPassword().equals(passwordChangeDto.getConfirmNewPassword())) {
            userManager.changePassword(passwordChangeDto.getOldPassword(), passwordChangeDto.getNewPassword());
            return Response.ok().build();
        } else {
            throw new PasswordMatchFailed("New password and new confrim password do not match");
        }

    }

    @POST
    @Path("/client")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT", "NONE"})
    public Response createClient(@Valid ClientDto clientDto) throws ClientValidationFailed {
        Client client = (Client) userDtoMapper.toUser(clientDto);
        client = userManager.registerClient(client.getFirstName(), client.getLastName(), client.getPersonalId(), client.getAddress(), client.getLogin(), client.getPassword());
        return Response.ok().entity(client).build();
    }

    @POST
    @Path("/admin")
    @RolesAllowed({"ADMIN"})
    public Response createAdmin(@Valid AdminDto adminDto) throws AdminValidationFailed {
        Admin admin = (Admin) userDtoMapper.toUser(adminDto);
        admin = userManager.registerAdmin(admin.getLogin(), admin.getPassword());
        return Response.ok().entity(admin).build();
    }

    @POST
    @Path("/manager")
    @RolesAllowed({"ADMIN", "MANAGER"})
    public Response createManager(@Valid ManagerDto managerDto) throws ManagerValidationFailed {
        Manager manager = (Manager) userDtoMapper.toUser(managerDto);
        manager = userManager.registerManager(manager.getLogin(), manager.getPassword());
        return Response.ok().entity(manager).build();
    }

    @PUT
    @Path("/client/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    public Response updateClient(@PathParam("uuid") UUID id, @Valid ClientDto clientDto, @Context HttpServletRequest request) throws UserWithGivenIdNotFound, ParseException, JOSEException {
        String jws = request.getHeader("If-Match");
        if (jws == null) {
            return Response.status(400).build();
        }
        if (userManager.getUserById(id) == null) {
            return Response.status(404).build();
        }
        Client client = (Client) userDtoMapper.toUser(clientDto);
        userManager.updateUser(id, jws, client.getFirstName(), client.getLastName(), client.getAddress(), client.getLogin(), client.getPassword(), client.getAccessLevel());
        return Response.ok().build();
    }

    @PUT
    @Path("/admin/{uuid}")
    @RolesAllowed({"ADMIN"})
    public Response updateAdmin(@PathParam("uuid") UUID id, @Valid AdminDto adminDto, @Context HttpServletRequest request) throws UserWithGivenIdNotFound, ParseException, JOSEException {
        String jws = request.getHeader("If-Match");
        if (jws == null) {
            return Response.status(400).build();
        }
        if (userManager.getUserById(id) == null) {
            return Response.status(404).build();
        }
        Admin admin = (Admin) userDtoMapper.toUser(adminDto);
        userManager.updateUser(id, jws, null, null, null, admin.getLogin(), admin.getPassword(), admin.getAccessLevel());
        return Response.ok().build();
    }

    @PUT
    @Path("/manager/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    public Response updateUser(@PathParam("uuid") UUID id, @Valid ManagerDto managerDto, @Context HttpServletRequest request) throws UserWithGivenIdNotFound, ParseException, JOSEException {
        String jws = request.getHeader("If-Match");
        if (jws == null) {
            return Response.status(400).build();
        }
        if (userManager.getUserById(id) == null) {
            return Response.status(404).build();
        }
        Manager manager = (Manager) userDtoMapper.toUser(managerDto);
        userManager.updateUser(id, jws, null, null, null, manager.getLogin(), manager.getPassword(), manager.getAccessLevel());
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT", "NONE"})
    public Response getUsers() {
        return Response.ok().entity(userManager.getAllUsers()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    public Response getUsersByPartOfLogin(String partOfLogin) {
        return Response.ok().entity(userManager.findClientsByLoginPart(partOfLogin)).build();
    }

    @DELETE
    @Path("/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER"})
    public Response deleteUser(@PathParam("uuid") UUID userId) throws UserWithGivenIdNotFound {
        try {
            userManager.getUserById(userId);
            userManager.deleteUser(userId);
        } catch (UserWithGivenIdNotFound e) {
            return Response.status(404).build();
        }
        return Response.ok().build();
    }

    @GET
    @Path("/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    public Response getUser(@PathParam("uuid") UUID userId) throws UserWithGivenIdNotFound {
        if (userManager.getUserById(userId) == null) {
            return Response.status(404).build();
        }
        return Response.ok().entity(userManager.getUserById(userId)).build();
    }

    @GET
    @Path("/client/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    public Response getClient(@PathParam("uuid") UUID userId) throws UserWithGivenIdNotFound {
        if (userManager.getClientById(userId) == null) {
            return Response.status(404).build();
        }
        return Response.ok().entity(userManager.getClientById(userId)).build();
    }

    @PUT
    @Path("/client/activate/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER"})
    public Response activateUser(@PathParam("uuid") UUID userId, @Context HttpServletRequest request) throws UserWithGivenIdNotFound, ParseException, JOSEException {
        String jws = request.getHeader("If-Match");
        if (jws == null) {
            return Response.status(400).build();
        }
        if (userManager.getUserById(userId) == null) {
            return Response.status(404).build();
        }
        userManager.activateUser(userId, jws);
        return Response.ok().build();
    }

    @PUT
    @Path("/client/deactivate/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER"})
    public Response deactivateUser(@PathParam("uuid") UUID userId, @Context HttpServletRequest request) throws UserWithGivenIdNotFound, ParseException, JOSEException {
        String jws = request.getHeader("If-Match");
        if (jws == null) {
            return Response.status(400).build();
        }
        if (userManager.getUserById(userId) == null) {
            return Response.status(404).build();
        }
        userManager.deactivateUser(userId, jws);
        return Response.ok().build();
    }

}

