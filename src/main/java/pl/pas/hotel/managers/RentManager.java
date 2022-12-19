package pl.pas.hotel.managers;

import jakarta.ejb.Stateless;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.pas.hotel.model.rent.Rent;
import pl.pas.hotel.model.room.Room;
import pl.pas.hotel.model.user.client.Client;
import pl.pas.hotel.repositoriesImplementation.RentRepository;
import pl.pas.hotel.repositoriesImplementation.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Stateless
public class RentManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(Slf4j.class);
    private final RentRepository rentRepository;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final UserRepository userRepository;

    public RentManager() {
        this.rentRepository = new RentRepository();
        this.userRepository = new UserRepository();
    }

    public Rent rentRoom(Client client, Room room, LocalDateTime beginTime, LocalDateTime endTime) {
        if (validator.validate(client).isEmpty() && validator.validate(room).isEmpty()) {
            if (beginTime.isBefore(endTime)) {
                    final List<Rent> rents = rentRepository.getCurrentRentsByRoom(room.getRoomId(), beginTime, endTime);
                    if (rents.isEmpty()) {
                        final UUID rentId = rentRepository.createRent(beginTime, endTime, client, room);
                        return rentRepository.getRentById(rentId);
                    } else {
                        LOGGER.warn("Room {} is already reserved", room.getRoomNumber());
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
        if (rent != null) {
            rentRepository.endRent(id);
        } else {
            LOGGER.warn("Rent {} does not exist in the database", rent.getId());
        }
    }


}
