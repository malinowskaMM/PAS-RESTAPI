package pl.pas.hotel.manager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.pas.hotel.model.rent.Rent;
import pl.pas.hotel.model.room.Room;
import pl.pas.hotel.model.user.client.Client;
import pl.pas.hotel.repositoryImpl.RentRepository;
import pl.pas.hotel.repositoryImpl.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
        if (validator.validate(client).isEmpty() && validator.validate(room).isEmpty()) {
            if (beginTime.isBefore(endTime)) {
                try {
                    entityManager.getTransaction().begin();
                    entityManager.lock(room, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
                    final List<Rent> rents = rentRepository.getCurrentRentsByRoom(room.getRoomId(), beginTime, endTime);
                    if (rents.isEmpty()) {
                        final UUID rentId = rentRepository.createRent(beginTime, endTime, client, room);
                        entityManager.getTransaction().commit();
                        return rentRepository.getRentById(rentId);
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

    public void endRoomRent(UUID id) {
        final Rent rent = rentRepository.getRentById(id);
        entityManager.getTransaction().begin();
        if (rent != null) {
            rentRepository.endRent(id);
            entityManager.getTransaction().commit();
        } else {
            LOGGER.warn("Rent {} does not exist in the database", rent.getId());
            entityManager.getTransaction().rollback();
        }
    }


}
