package pl.pas.hotel.managers;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import pl.pas.hotel.exceptions.RoomValidationFailed;
import pl.pas.hotel.exceptions.RoomWithGivenIdNotExist;
import pl.pas.hotel.model.room.Room;
import pl.pas.hotel.repositoriesImplementation.RoomRepository;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@Stateless
public class RoomManager {
    @Inject
    private RoomRepository roomRepository;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public synchronized Room createRoom(Integer roomNumber, Double basePrice, int roomCapacity) throws RoomValidationFailed {
        final Room room = new Room(roomNumber, basePrice, roomCapacity);
        if (validator.validate(room).isEmpty()) {
            return roomRepository.createRoom(room.getRoomNumber(), room.getPrice(), room.getRoomCapacity());
        } else {
            throw new RoomValidationFailed("Cannot create room with given parameters");
        }
    }

    public synchronized void removeRoom(UUID id) throws RoomWithGivenIdNotExist {
        final Room room1 = roomRepository.getRoomById(id);
        if (room1 == null) {
            throw new RoomWithGivenIdNotExist("Cannot delete room");
        } else {
            roomRepository.deleteRoom(room1.getRoomId());
        }

    }

    public synchronized void updateRoom(UUID id, Integer roomNumber, Double basePrice, int roomCapacity) throws RoomWithGivenIdNotExist {
        final Room room1 = roomRepository.getRoomById(id);
        if (room1 == null) {
            throw new RoomWithGivenIdNotExist("Cannot update room");
        } else {
            roomRepository.modifyRoom(id, roomNumber, basePrice, roomCapacity);
        }
    }

    public Room getRoomById(String id) throws RoomWithGivenIdNotExist {
        final Room room = roomRepository.getRoomById(UUID.fromString(id));
        if (room == null) {
            throw new RoomWithGivenIdNotExist("Cannot update room");
        }
        return room;
    }

    public List<Room> getRooms(Predicate<Room> predicate) {
        return roomRepository.getRoomsBy(predicate);
    }

    public List<Room> getAllRooms() {
        return roomRepository.getRooms();
    }


}
