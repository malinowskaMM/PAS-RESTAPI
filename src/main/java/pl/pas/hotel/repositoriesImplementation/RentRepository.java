package pl.pas.hotel.repositoriesImplementation;

import jakarta.enterprise.context.ApplicationScoped;
import pl.pas.hotel.model.rent.Rent;
import pl.pas.hotel.model.room.Room;
import pl.pas.hotel.model.user.client.Client;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.synchronizedList;

@ApplicationScoped
public class RentRepository implements pl.pas.hotel.repositories.RentRepository {

    private final List<Rent> rents = synchronizedList(new ArrayList<>());

    @Override
    public Rent createRent(LocalDateTime beginTime, LocalDateTime endTime, Client client, Room room) {
        if(client.isActive() && getCurrentRentsByRoom(room.getUuid(), beginTime, endTime).isEmpty()) {
            Rent rent = new Rent(beginTime, endTime, client, room);
            rents.add(rent);
            return rent;
        }
        return null;
    }

    @Override
    public void removeRent(UUID id) {
        Rent removed = getRentById(id);
        if(removed != null) {
            rents.remove(removed);
        }
    }

    @Override
    public void endRent(UUID id) {
        Rent rent = getRentById(id);
        if(rent != null) {
            rent.endRent();
        }
    }

    @Override
    public List<Rent> getRentsByClient(UUID clientId) {
        return getRents().stream().filter(rent -> rent.getClient().getUuid().equals(clientId)).toList();
    }

    @Override
    public List<Rent> getRentsByRoom(UUID roomId) {
        return getRents().stream().filter(rent -> rent.getRoom().getUuid().equals(roomId)).toList();
    }


    //TODO:implementation method
    @Override
    public List<Rent> getCurrentRentsByRoom(UUID roomId, LocalDateTime beginTime, LocalDateTime endTime) {
        return getRentsByRoom(roomId);
    }

    @Override
    public List<Rent> getRents() {
        return rents.stream().toList();
    }

    @Override
    public Rent getRentById(UUID id) {
        Optional<Rent> rentOptional = rents.stream().filter(rent -> rent.getId().equals(id)).findFirst();
        return rentOptional.orElse(null);
    }
}
