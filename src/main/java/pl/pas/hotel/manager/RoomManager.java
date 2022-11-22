package pl.pas.hotel.manager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.RollbackException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.pas.hotel.model.room.Room;
import pl.pas.hotel.repositoryImpl.RoomRepository;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class RoomManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(Slf4j.class);
    @PersistenceContext
    private final EntityManager entityManager;
    private final RoomRepository roomRepository;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public RoomManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.roomRepository = new RoomRepository(entityManager);
    }


    public synchronized Room createRoom(Integer roomNumber, Double basePrice, int roomCapacity) {
        final Room room = new Room(roomNumber, basePrice, roomCapacity);
        if (validator.validate(room).isEmpty()) {
            try {
                entityManager.getTransaction().begin();
                final UUID saved = roomRepository.createRoom(room.getRoomNumber(), room.getPrice(), room.getRoomCapacity());
                entityManager.getTransaction().commit();
                LOGGER.debug("Room saved successfully");
                return roomRepository.getRoomById(saved);
            } catch (RollbackException e) {
                LOGGER.error("Transaction failed", e);
                entityManager.getTransaction().rollback();
            }
        } else {
            LOGGER.warn("Room {} validation failed", room.getRoomNumber());
        }
        return null;
    }

    public synchronized void removeRoom(UUID id) {
        entityManager.getTransaction().begin();
        final Room room1 = roomRepository.getRoomById(id);
        if (room1 == null) {
            LOGGER.warn("Room {} does not exist in the database", id);
            entityManager.getTransaction().rollback();
        } else {
            roomRepository.deleteRoom(room1.getRoomId());
            entityManager.getTransaction().commit();
            LOGGER.debug("Room deleted successfully");
        }

    }

    public synchronized void updateRoom(UUID id, Integer roomNumber, Double basePrice, int roomCapacity) {
        entityManager.getTransaction().begin();
        final Room room1 = roomRepository.getRoomById(id);
        if (room1 == null) {
            LOGGER.warn("Room {} does not exist in the database", id);
            entityManager.getTransaction().rollback();
        } else {
            roomRepository.modifyRoom(id, roomNumber, basePrice, roomCapacity);
            if (!validator.validate(roomRepository.getRoomById(id)).isEmpty()) {
                LOGGER.warn("Room {} validation failed", id);
                entityManager.getTransaction().rollback();
            }
            entityManager.getTransaction().commit();
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
