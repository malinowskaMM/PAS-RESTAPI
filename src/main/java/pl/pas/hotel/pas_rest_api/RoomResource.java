package pl.pas.hotel.pas_rest_api;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.pas.hotel.managers.RoomManager;
import pl.pas.hotel.model.room.Room;

import java.util.UUID;

@Path("/rooms")
public class RoomResource {

    @Inject
    private RoomManager roomManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRooms() {
        return Response.ok().entity(roomManager.getAllRooms()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createRoom(@Valid Room room) {
        roomManager.createRoom(room.getRoomNumber(), room.getPrice(), room.getRoomCapacity());
        return Response.ok().entity(room).build();
    }

    @DELETE
    @Path("/{uuid}")
    public Response deleteRoom(@PathParam("uuid") UUID roomId) {
        if(roomManager.getRoomById(roomId.toString()) == null) {
            return Response.status(404).build();
        }
        roomManager.removeRoom(roomId);
        return Response.ok().build();
    }

    @GET
    @Path("/{uuid}")
    public Response getRoom(@PathParam("uuid") UUID roomId) {
        if(roomManager.getRoomById(roomId.toString()) == null) {
            return Response.status(404).build();
        }
        return Response.ok().entity(roomManager.getRoomById(roomId.toString())).build();
    }

    @PUT
    @Path("/{uuid}")
    public Response updateRoom(@PathParam("uuid") UUID roomId, Room room) {
        if(roomManager.getRoomById(roomId.toString()) == null) {
            return Response.status(404).build();
        }
        roomManager.updateRoom(roomId, room.getRoomNumber(), room.getPrice(), room.getRoomCapacity());
        return Response.ok().build();
    }
}
