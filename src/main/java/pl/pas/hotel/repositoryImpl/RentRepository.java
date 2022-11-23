package pl.pas.hotel.repositoryImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import pl.pas.hotel.model.rent.Rent;
import pl.pas.hotel.model.room.Room;
import pl.pas.hotel.model.user.client.Client;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class RentRepository implements pl.pas.hotel.repository.RentRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public UUID createRent(LocalDateTime beginTime, LocalDateTime endTime, Client client, Room room) {
        if(client.isActive() && getCurrentRentsByRoom(room.getRoomId(), beginTime, endTime).isEmpty()) {
            Rent rent = new Rent(beginTime, endTime, client, room);
            entityManager.persist(rent);
            return rent.getId();
        }
        return null;
    }

    @Override
    public void removeRent(UUID id) {
        Rent removed = getRentById(id);
        if(removed != null) {
            entityManager.remove(removed);
        }
    }

    @Override
    public void endRent(UUID id) {
        Rent rent = getRentById(id);
        if(rent != null) {
            rent.endRent();
            entityManager.merge(rent);
        }
    }

    @Override
    public List<Rent> getRentsByClient(UUID clientId) {
        return getRents().stream().filter(rent -> rent.getClient().getId().equals(clientId)).toList();
    }

    @Override
    public List<Rent> getRentsByRoom(UUID roomId) {
        return getRents().stream().filter(rent -> rent.getRoom().getRoomId().equals(roomId)).toList();
    }

    //TO DO
    @Override
    public List<Rent> getCurrentRentsByRoom(UUID roomId, LocalDateTime beginTime, LocalDateTime endTime) {
        return null;
    }

    @Override
    public List<Rent> getRents() {
        return entityManager.createQuery("select r from Rent r", Rent.class).getResultList();
    }

    @Override
    public Rent getRentById(UUID id) {
        return entityManager.find(Rent.class, id);
    }
}
