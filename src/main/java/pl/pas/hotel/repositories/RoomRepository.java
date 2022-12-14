package pl.pas.hotel.repositories;

import pl.pas.hotel.model.room.Room;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public interface RoomRepository {

    Room createRoom(Integer roomNumber, Double price, Integer roomCapacity);
    List<Room> getRooms();
    List<Room> getRoomsBy(Predicate<Room> predicate);
    Room modifyRoom(UUID id, Integer roomNumber, Double price, Integer roomCapacity);
    void deleteRoom(UUID id);
    Room getRoomById(UUID id);
}
