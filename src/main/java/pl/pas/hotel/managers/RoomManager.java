package pl.pas.hotel.managers;

import com.nimbusds.jose.JOSEException;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;
import org.json.simple.JSONObject;
import pl.pas.hotel.auth.JwsGenerator;
import pl.pas.hotel.exceptions.RoomValidationFailed;
import pl.pas.hotel.exceptions.RoomWithGivenIdNotFound;
import pl.pas.hotel.model.room.Room;
import pl.pas.hotel.repositoriesImplementation.RentRepository;
import pl.pas.hotel.repositoriesImplementation.RoomRepository;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@Stateless
public class RoomManager {
    @Inject
    private RoomRepository roomRepository;

    @Inject
    private RentRepository rentRepository;

    private JwsGenerator jwsGenerator = new JwsGenerator();

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public synchronized Room createRoom(Integer roomNumber, Double basePrice, int roomCapacity) throws RoomValidationFailed {
        final Room room = new Room(roomNumber, basePrice, roomCapacity);
        if (validator.validate(room).isEmpty()) {
            return roomRepository.createRoom(room.getRoomNumber(), room.getPrice(), room.getRoomCapacity());
        } else {
            throw new RoomValidationFailed("Cannot create room with given parameters");
        }
    }

    public synchronized void removeRoom(UUID id) throws RoomWithGivenIdNotFound {
        final Room room1 = roomRepository.getRoomById(id);
        if (room1 == null || !rentRepository.getRentsByRoom(id).isEmpty()) {
            throw new RoomWithGivenIdNotFound("Cannot delete room");
        } else {
            roomRepository.deleteRoom(room1.getUuid());
        }

    }

    public synchronized void updateRoom(UUID id, String jws, Integer roomNumber, Double basePrice, int roomCapacity) throws RoomWithGivenIdNotFound, ParseException, JOSEException {
        final Room room1 = roomRepository.getRoomById(id);
        if (room1 == null) {
            throw new RoomWithGivenIdNotFound("Cannot update room");
        } else {
            if (this.jwsGenerator.verify(jws)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("uuid", room1.getUuid().toString());
                String newJwt = this.jwsGenerator.generateJws(jsonObject.toString());
                if(newJwt.equals(jws)) {
                    roomRepository.modifyRoom(room1.getUuid(), roomNumber, basePrice, roomCapacity);
                }
            }
        }
    }

    public Room getRoomById(UUID id) {
        final Room room = roomRepository.getRoomById(id);
        if (room == null) {
            throw new NotFoundException("Cannot update room");
        }
        return room;
    }

    public List<Room> getRooms(Predicate<Room> predicate) {
        return roomRepository.getRoomsBy(predicate);
    }

    public List<Room> getAllRooms() {
        return roomRepository.getRooms();
    }

    public String getJws(UUID id) throws JOSEException {
        Room room = roomRepository.getRoomById(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", room.getUuid());
        return jwsGenerator.generateJws(jsonObject.toString());
    }


}
