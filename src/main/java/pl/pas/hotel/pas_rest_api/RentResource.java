package pl.pas.hotel.pas_rest_api;

import com.nimbusds.jose.JOSEException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.pas.hotel.dto.rent.RentDto;
import pl.pas.hotel.dto.rent.mapper.RentDtoMapper;
import pl.pas.hotel.exceptions.*;
import pl.pas.hotel.managers.RentManager;
import pl.pas.hotel.model.rent.Rent;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.UUID;

@RequestScoped
@Path("/rents")
public class RentResource {

    @Inject
    private RentManager rentManager;

    @Inject
    private RentDtoMapper rentDtoMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "MANAGER"})
    public Response getRents() {
        return Response.ok().entity(rentManager.getRents()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/room/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER"})
    public Response getRentsByRoom(@PathParam("uuid") UUID roomId) throws RoomWithGivenIdNotFound {
        return Response.ok().entity(rentManager.getRentsByRoomId(roomId)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/client/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER"})
    public Response getRentsByClient(@PathParam("uuid") UUID clientId) throws UserWithGivenIdNotFound {
        return Response.ok().entity(rentManager.getRentsByClientId(clientId)).build();
    }

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/startDate")
    @RolesAllowed({"ADMIN", "MANAGER"})
    public Response getRentsByStartDate(@QueryParam("startDate") String startDate) {
        LocalDateTime date = LocalDateTime.parse(startDate);
        return Response.ok().entity(rentManager.getRentsByStartDate(date)).build();
    }

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/endDate")
    @RolesAllowed({"ADMIN", "MANAGER"})
    public Response getRentsByEndDate(@QueryParam("endDate") String endDate) {
        LocalDateTime date = LocalDateTime.parse(endDate);
        return Response.ok().entity(rentManager.getRentsByEndDate(date)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/archived")
    @RolesAllowed({"ADMIN", "MANAGER"})
    public Response getPastRents() {
        return Response.ok().entity(rentManager.getPastRents()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/current/client/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER"})
    public Response getCurrentRentsByClientId(@PathParam("uuid") UUID clientId) {
        return Response.ok().entity(rentManager.getCurrentRentsByClientId(clientId)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/archived/client/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER"})
    public Response getPastRentsByClientId(@PathParam("uuid") UUID clientId) {
        return Response.ok().entity(rentManager.getPastRentsByClientId(clientId)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/current/room/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER"})
    public Response getCurrentRentsByRoomId(@PathParam("uuid") UUID roomId) {
        return Response.ok().entity(rentManager.getCurrentRentsByRoomId(roomId)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/archived/room/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER"})
    public Response getPastRentsByRoomId(@PathParam("uuid") UUID roomId) {
        return Response.ok().entity(rentManager.getPastRentsByRoomId(roomId)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/current")
    @RolesAllowed({"ADMIN", "MANAGER"})
    public Response getCurrentRents() {
        return Response.ok().entity(rentManager.getCurrentRents()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "MANAGER", "USER"})
    public Response rentRoom(@Valid RentDto rentDto) throws RoomWithGivenIdNotFound, ClientWithGivenIdNotFound, RoomNotAvailable, RentValidationFailed, DateTimeValidationFailed {
        Rent rent = rentDtoMapper.toRent(rentDto);
        Rent rentResult = rentManager.rentRoom(rent.getClientId(), rent.getRoomId(), rent.getBeginTime(), rent.getEndTime());
        return Response.ok().entity(rentResult).build();
    }

    @PUT
    @Path("/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER", "USER"})
    public Response endRent(@PathParam("uuid") UUID rentId, @Context HttpServletRequest request) throws RentWithGivenIdNotFound, ParseException, JOSEException {
        String jws = request.getHeader("If-Match");
        if (jws == null) {
            return Response.status(400).build();
        }
        if(rentManager.getRent(rentId) == null) {
            return Response.status(404).build();
        }
        rentManager.endRoomRent(rentId, jws);
        return Response.ok().build();
    }

    @GET
    @Path("/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER", "USER"})
    public Response getRent(@PathParam("uuid") UUID rentId) throws RentWithGivenIdNotFound {
        if(rentManager.getRent(rentId) == null) {
            return Response.status(404).build();
        }
        return Response.ok().entity(rentManager.getRent(rentId)).build();
    }

    @DELETE
    @Path("/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER"})
    public Response deleteRent(@PathParam("uuid") UUID rentId) throws RentWithGivenIdNotFound {
        if(rentManager.getRent(rentId) == null) {
            return Response.status(404).build();
        }
        rentManager.removeRent(rentId);
        return Response.ok().build();
    }
}
