package pl.pas.hotel.managers;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.pas.hotel.model.room.Room;
import pl.pas.hotel.repositoriesImplementation.RoomRepository;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@Stateless
public class RoomManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(Slf4j.class);
    @Inject
    private RoomRepository roomRepository;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public synchronized Room createRoom(Integer roomNumber, Double basePrice, int roomCapacity) {
        final Room room = new Room(roomNumber, basePrice, roomCapacity);
        if (validator.validate(room).isEmpty()) {
            LOGGER.debug("Room saved successfully");
            return roomRepository.createRoom(room.getRoomNumber(), room.getPrice(), room.getRoomCapacity());
        } else {
            LOGGER.warn("Room {} validation failed", room.getRoomNumber());
        }
        return null;
    }

    public synchronized void removeRoom(UUID id) {
        final Room room1 = roomRepository.getRoomById(id);
        if (room1 == null) {
            LOGGER.warn("Room {} does not exist in the database", id);
        } else {
            roomRepository.deleteRoom(room1.getRoomId());
            LOGGER.debug("Room deleted successfully");
        }

    }

    public synchronized void updateRoom(UUID id, Integer roomNumber, Double basePrice, int roomCapacity) {
        final Room room1 = roomRepository.getRoomById(id);
        if (room1 == null) {
            LOGGER.warn("Room {} does not exist in the database", id);
        } else {
            roomRepository.modifyRoom(id, roomNumber, basePrice, roomCapacity);
            if (!validator.validate(roomRepository.getRoomById(id)).isEmpty()) {
                LOGGER.warn("Room {} validation failed", id);
            }
            LOGGER.debug("Room modified successfully");
        }
    }

    public Room getRoomById(String id) {
        return roomRepository.getRoomById(UUID.fromString(id));
    }

    public List<Room> getRooms(Predicate<Room> predicate) {
        return roomRepository.getRoomsBy(predicate);
    }

    public List<Room> getAllRooms() {
        return roomRepository.getRooms();
    }


}
