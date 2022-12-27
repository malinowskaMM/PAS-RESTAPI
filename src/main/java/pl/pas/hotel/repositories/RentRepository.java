package pl.pas.hotel.repositories;

import pl.pas.hotel.model.rent.Rent;
import pl.pas.hotel.model.room.Room;
import pl.pas.hotel.model.user.client.Client;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface RentRepository {
    Rent createRent(LocalDateTime beginTime, LocalDateTime endTime, Client client, Room room);
    void removeRent(UUID id);
    void endRent(UUID id);
    List<Rent> getRentsByClient(UUID clientId);
    List<Rent> getRentsByRoom(UUID roomId);
    List<Rent> getCurrentRentsByRoom(UUID roomId, LocalDateTime beginTime, LocalDateTime endTime);
    List<Rent> getRents();
    Rent getRentById(UUID id);
}
