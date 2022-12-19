package pl.pas.hotel.pas_rest_api;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.pas.hotel.managers.RentManager;
import pl.pas.hotel.model.rent.Rent;
import pl.pas.hotel.model.room.Room;
import pl.pas.hotel.model.user.client.Client;

import java.time.LocalDateTime;
import java.util.UUID;

@Path("/rents")
public class RentResource {

    @Inject
    private RentManager rentManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRents() {
        return Response.ok().entity(rentManager.getRents()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response rentRoom(Client client, Room room, LocalDateTime beginTime, LocalDateTime endTime) {
        Rent rent = rentManager.rentRoom(client, room, beginTime, endTime);
        return Response.ok().entity(rent).build();
    }

    @PUT
    @Path("/{uuid}")
    public Response endRent(@PathParam("uuid") UUID rentId) {
        if(rentManager.getRent(rentId) == null) {
            return Response.status(404).build();
        }
        rentManager.endRoomRent(rentId);
        return Response.ok().build();
    }

    @GET
    @Path("/{uuid}")
    public Response getRent(@PathParam("uuid") UUID rentId) {
        if(rentManager.getRent(rentId) == null) {
            return Response.status(404).build();
        }
        rentManager.getRent(rentId);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{uuid}")
    public Response deleteRent(@PathParam("uuid") UUID rentId) {
        if(rentManager.getRent(rentId) == null) {
            return Response.status(404).build();
        }
        rentManager.removeRent(rentId);
        return Response.ok().build();
    }
}
