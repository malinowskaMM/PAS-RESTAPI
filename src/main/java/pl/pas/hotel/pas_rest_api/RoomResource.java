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
import org.json.JSONObject;
import pl.pas.hotel.auth.JwsGenerator;
import pl.pas.hotel.dto.room.RoomDto;
import pl.pas.hotel.dto.room.mapper.RoomDtoMapper;
import pl.pas.hotel.exceptions.RoomValidationFailed;
import pl.pas.hotel.exceptions.RoomWithGivenIdNotFound;
import pl.pas.hotel.managers.RoomManager;
import pl.pas.hotel.model.room.Room;
import pl.pas.hotel.model.user.User;

import java.text.ParseException;
import java.util.UUID;

@Path("/rooms")
public class RoomResource {

    @Inject
    JwsGenerator jwsGenerator;

    @Inject
    private RoomManager roomManager;

    @Inject
    private RoomDtoMapper roomDtoMapper;

    @GET
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRooms() {
        return Response.ok().entity(roomManager.getAllRooms()).build();
    }

    @RolesAllowed({"ADMIN"})
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createRoom(@Valid RoomDto roomDto) throws RoomValidationFailed {
        Room room = roomDtoMapper.toRoom(roomDto);
        Room result = roomManager.createRoom(room.getRoomNumber(), room.getPrice(), room.getRoomCapacity());
        return Response.ok().entity(result).build();
    }

    @DELETE
    @Path("/{uuid}")
    @RolesAllowed({"ADMIN"})
    public Response deleteRoom(@PathParam("uuid") UUID roomId) throws RoomWithGivenIdNotFound {
        if(roomManager.getRoomById(roomId) == null) {
            return Response.status(404).build();
        }
        roomManager.removeRoom(roomId);
        return Response.ok().build();
    }

    @GET
    @Path("/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    public Response getRoom(@PathParam("uuid") UUID roomId) throws RoomWithGivenIdNotFound, JOSEException {
        if(roomManager.getRoomById(roomId) == null) {
            return Response.status(404).build();
        }
        return Response.ok().entity(roomManager.getRoomById(roomId)).header("ETag", getJwsFromRoom(roomId)).build();
    }

    @PUT
    @Path("/{uuid}")
    @RolesAllowed({"ADMIN", "MANAGER", "CLIENT"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateRoom(@PathParam("uuid") UUID roomId, RoomDto roomDto, @Context HttpServletRequest request) throws RoomWithGivenIdNotFound, ParseException, JOSEException {
        String jws = request.getHeader("If-Match");
        if (jws == null) {
            return Response.status(400).build();
        }
        if(roomManager.getRoomById(roomId) == null) {
            return Response.status(404).build();
        }
        Room room = roomDtoMapper.toRoom(roomDto);
        roomManager.updateRoom(roomId, jws, room.getRoomNumber(), room.getPrice(), room.getRoomCapacity());
        return Response.ok().build();
    }

    public String getJwsFromRoom(UUID id) throws NotFoundException, JOSEException {
        Room room = roomManager.getRoomById(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uuid", room.getUuid().toString());
        return this.jwsGenerator.generateJws(jsonObject.toString());
    }
}
