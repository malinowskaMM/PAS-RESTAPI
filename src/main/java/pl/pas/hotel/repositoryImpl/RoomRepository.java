package pl.pas.hotel.repositoryImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import pl.pas.hotel.model.room.Room;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class RoomRepository implements pl.pas.hotel.repository.RoomRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public UUID createRoom(Integer roomNumber, Double price, Integer roomCapacity) {
        Room room = new Room(roomNumber, price, roomCapacity);
        entityManager.persist(room);
        return room.getRoomId();
    }

    @Override
    public List<Room> getRooms() {
        return entityManager.createQuery("select r from Room r", Room.class).getResultList();
    }

    @Override
    public List<Room> getRoomsBy(Predicate<Room> predicate) {
        return getRooms().stream().filter(predicate).toList();
    }

    @Override
    public Room modifyRoom(UUID id, Integer roomNumber, Double price, Integer roomCapacity) {
        Room modifiedRoom = entityManager.find(Room.class, id);
        if(modifiedRoom != null) {
            modifiedRoom.setRoomNumber(roomNumber);
            modifiedRoom.setPrice(price);
            modifiedRoom.setRoomCapacity(roomCapacity);
            entityManager.merge(modifiedRoom);
            return modifiedRoom;
        }
        return null;
    }

    @Override
    public void deleteRoom(UUID id) {
        Room removed = entityManager.find(Room.class, id);
        if(removed != null) {
            entityManager.remove(removed);
        }
    }

    @Override
    public Room getRoomById(UUID id) {
        return entityManager.find(Room.class, id);
    }
}
