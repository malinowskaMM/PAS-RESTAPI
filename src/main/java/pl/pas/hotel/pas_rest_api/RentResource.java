package pl.pas.hotel.pas_rest_api;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.pas.hotel.dto.rent.RentDto;
import pl.pas.hotel.dto.rent.mapper.RentDtoMapper;
import pl.pas.hotel.exceptions.*;
import pl.pas.hotel.managers.RentManager;
import pl.pas.hotel.model.rent.Rent;

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
    public Response getRents() {
        return Response.ok().entity(rentManager.getRents()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/room/{uuid}")
    public Response getRentsByRoom(@PathParam("uuid") UUID roomId) throws RoomWithGivenIdNotFound {
        return Response.ok().entity(rentManager.getRentsByRoomId(roomId)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/client/{uuid}")
    public Response getRentsByClient(@PathParam("uuid") UUID clientId) throws UserWithGivenIdNotFound {
        return Response.ok().entity(rentManager.getRentsByClientId(clientId)).build();
    }

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/startDate")
    public Response getRentsByStartDate(@QueryParam("startDate") String startDate) {
        LocalDateTime date = LocalDateTime.parse(startDate);
        return Response.ok().entity(rentManager.getRentsByStartDate(date)).build();
    }

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/endDate")
    public Response getRentsByEndDate(@QueryParam("endDate") String endDate) {
        LocalDateTime date = LocalDateTime.parse(endDate);
        return Response.ok().entity(rentManager.getRentsByEndDate(date)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/archived")
    public Response getPastRents() {
        return Response.ok().entity(rentManager.getPastRents()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/current/client/{uuid}")
    public Response getCurrentRentsByClientId(@PathParam("uuid") UUID clientId) {
        return Response.ok().entity(rentManager.getCurrentRentsByClientId(clientId)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/archived/client/{uuid}")
    public Response getPastRentsByClientId(@PathParam("uuid") UUID clientId) {
        return Response.ok().entity(rentManager.getPastRentsByClientId(clientId)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/current/room/{uuid}")
    public Response getCurrentRentsByRoomId(@PathParam("uuid") UUID roomId) {
        return Response.ok().entity(rentManager.getCurrentRentsByRoomId(roomId)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/archived/room/{uuid}")
    public Response getPastRentsByRoomId(@PathParam("uuid") UUID roomId) {
        return Response.ok().entity(rentManager.getPastRentsByRoomId(roomId)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/current")
    public Response getCurrentRents() {
        return Response.ok().entity(rentManager.getCurrentRents()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response rentRoom(@Valid RentDto rentDto) throws RoomWithGivenIdNotFound, ClientWithGivenIdNotFound, RoomNotAvailable, RentValidationFailed, DateTimeValidationFailed {
        Rent rent = rentDtoMapper.toRent(rentDto);
        Rent rentResult = rentManager.rentRoom(rent.getClient(), rent.getRoom(), rent.getBeginTime(), rent.getEndTime());
        return Response.ok().entity(rentResult).build();
    }

    @PUT
    @Path("/{uuid}")
    public Response endRent(@PathParam("uuid") UUID rentId) throws RentWithGivenIdNotFound {
        if(rentManager.getRent(rentId) == null) {
            return Response.status(404).build();
        }
        rentManager.endRoomRent(rentId);
        return Response.ok().build();
    }

    @GET
    @Path("/{uuid}")
    public Response getRent(@PathParam("uuid") UUID rentId) throws RentWithGivenIdNotFound {
        if(rentManager.getRent(rentId) == null) {
            return Response.status(404).build();
        }
        return Response.ok().entity(rentManager.getRent(rentId)).build();
    }

    @DELETE
    @Path("/{uuid}")
    public Response deleteRent(@PathParam("uuid") UUID rentId) throws RentWithGivenIdNotFound {
        if(rentManager.getRent(rentId) == null) {
            return Response.status(404).build();
        }
        rentManager.removeRent(rentId);
        return Response.ok().build();
    }
}
