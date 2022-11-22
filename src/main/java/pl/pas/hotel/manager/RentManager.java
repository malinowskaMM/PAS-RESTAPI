package pl.pas.hotel.manager;

/*import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.pas.hotel.client.Client;
import pl.pas.hotel.user.UserRepository;

import pl.pas.hotel.room.Room;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class RentManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(Slf4j.class);
    private final RentRepository rentRepository;
    private final EntityManager entityManager;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final UserRepository userRepository;

    public RentManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.rentRepository = new RentRepository(entityManager);
        this.userRepository = new UserRepository(entityManager);
    }

    public Rent rentRoom(Client client, Room room, LocalDateTime beginTime, LocalDateTime endTime) {
        if (validator.validate(client).size() == 0 && validator.validate(room).size() == 0) {
            if (beginTime.isBefore(endTime)) {
                try {
                    entityManager.getTransaction().begin();
                    entityManager.lock(room, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
                    final List<Rent> rents = rentRepository.getRentsForRoom(room.getRoomNumber().toString(), beginTime, endTime);
                    if (rents.size() == 0) {
                        final Rent rent = rentRepository.save(new Rent(UUID.randomUUID(), beginTime, endTime, client, room, client.applyDiscount(room.getPrice())));
                        entityManager.getTransaction().commit();
                        return rent;
                    } else {
                        LOGGER.warn("Room {} is already reserved", room.getRoomNumber());
                        entityManager.getTransaction().rollback();
                    }
                } catch (IllegalArgumentException e) {
                entityManager.getTransaction().rollback();
            }
            } else {
                LOGGER.warn("Begin time of reservation {} is not earlier than end time of reservation {}", beginTime, endTime);
            }
        } else {
            LOGGER.error("Parameters validation failed");
        }
        return null;
    }

    public void endRoomRent(Rent rent) {
        if (validator.validate(rent).size() == 0) {
            entityManager.getTransaction().begin();
            final Rent rent1 = rentRepository.findById(rent.getId().toString());
            if(rent1 != null) {
                final Client client = rent1.getClient();
                client.setMoneySpent(rent1.client.getMoneySpent() + rent1.rentCost);
                rentRepository.remove(rent1);
                checkChangeClientType(client);
                entityManager.getTransaction().commit();
            } else {
                LOGGER.warn("Rent {} does not exist in the database", rent.getId());
                entityManager.getTransaction().rollback();
            }
        } else {
            LOGGER.error("Rent {} validation failed", rent.getId());
        }
    }

    public List<Rent> getAllClientRents(Client client) {
        return rentRepository.find(rent -> rent.getClient().equals(client));
    }

    public List<Rent> getRoomRent(Room room) {
        return rentRepository.find(rent -> rent.getRoom().equals(room));
    }

    public List<Rent> findRents(Predicate<Rent> predicate) {
        return rentRepository.find(predicate);
    }

    public String getAllRentsReport() {
        return rentRepository.getReport();
    }

    void checkChangeClientType(Client client) {
        if (client.getMoneySpent() > 100000) {
            if (client.getClientType().getClientTypeName().equals(ClientTypeName.SAPPHIRE)) {
                client.getClientType().setClientTypeName(ClientTypeName.DIAMOND);
            }
        } else if (client.getMoneySpent() > 50000) {
            if (client.getClientType().getClientTypeName().equals(ClientTypeName.EMERALD)) {
                client.getClientType().setClientTypeName(ClientTypeName.SAPPHIRE);
            }
        } else if (client.getMoneySpent() > 10000) {
            if (client.getClientType().getClientTypeName().equals(ClientTypeName.GOLD)) {
                client.getClientType().setClientTypeName(ClientTypeName.EMERALD);
            }
        } else if (client.getMoneySpent() > 5000) {
            if (client.getClientType().getClientTypeName().equals(ClientTypeName.REGULAR)) {
                client.getClientType().setClientTypeName(ClientTypeName.GOLD);
            }
        }
        userRepository.save(client);
    }
}*/
