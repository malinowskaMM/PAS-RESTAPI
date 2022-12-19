package pl.pas.hotel.repositoriesImplementation;

import jakarta.enterprise.context.ApplicationScoped;
import pl.pas.hotel.model.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import static java.util.Collections.synchronizedList;

@ApplicationScoped
public class RoomRepository implements pl.pas.hotel.repositories.RoomRepository {

    private final List<Room> rooms = synchronizedList(new ArrayList<>());

    @Override
    public UUID createRoom(Integer roomNumber, Double price, Integer roomCapacity) {
        Room room = new Room(roomNumber, price, roomCapacity);
        rooms.add(room);
        return room.getRoomId();
    }

    @Override
    public List<Room> getRooms() {
        return rooms.stream().toList();
    }

    @Override
    public List<Room> getRoomsBy(Predicate<Room> predicate) {
        return getRooms().stream().filter(predicate).toList();
    }

    @Override
    public Room modifyRoom(UUID id, Integer roomNumber, Double price, Integer roomCapacity) {
        Room modifiedRoom = getRoomById(id);
        if(modifiedRoom != null) {
            modifiedRoom.setRoomNumber(roomNumber);
            modifiedRoom.setPrice(price);
            modifiedRoom.setRoomCapacity(roomCapacity);
            return modifiedRoom;
        }
        return null;
    }

    @Override
    public void deleteRoom(UUID id) {
        Room removed = getRoomById(id);
        if(removed != null) {
            rooms.remove(removed);
        }
    }

    @Override
    public Room getRoomById(UUID id) {
        Optional<Room> roomOptional = rooms.stream().filter(room -> room.getRoomId().equals(id)).findFirst();
        return roomOptional.orElse(null);
    }
}
