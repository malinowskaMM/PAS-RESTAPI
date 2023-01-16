package pl.pas.hotel.repositoriesImplementation;

import jakarta.enterprise.context.ApplicationScoped;
import pl.pas.hotel.exceptions.RoomNotAvailable;
import pl.pas.hotel.model.rent.Rent;

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
    public Rent createRent(LocalDateTime beginTime, LocalDateTime endTime, String clientId, String roomId) throws RoomNotAvailable {
        if(getCurrentRentsByRoom(UUID.fromString(roomId), beginTime, endTime).isEmpty()) {
            Rent rent = new Rent(beginTime, endTime, clientId, roomId);
            rents.add(rent);
            return rent;
        }
        throw new RoomNotAvailable("Cannot rent room");
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

    public List<Rent> getRentsByClient(String login) {
        return getRents().stream().filter(rent -> rent.getLogin().equals(login)).toList();
    }

    @Override
    public List<Rent> getRentsByRoom(UUID roomId) {
        return getRents().stream().filter(rent -> rent.getRoomId().equals(roomId.toString())).toList();
    }


    @Override
    public List<Rent> getCurrentRentsByRoom(UUID roomId, LocalDateTime beginTime, LocalDateTime endTime) {
        return getRentsByRoom(roomId).stream().filter(
                rent -> ((rent.getBeginTime().isBefore(beginTime) && rent.getEndTime().isBefore(endTime) && rent.getEndTime().isAfter(beginTime))
                || (rent.getBeginTime().isAfter(beginTime) && rent.getEndTime().isBefore(endTime))
                || (rent.getBeginTime().isAfter(beginTime) && rent.getBeginTime().isBefore(endTime) && rent.getEndTime().isAfter(endTime))
        )).toList();
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
