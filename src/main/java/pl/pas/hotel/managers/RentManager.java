package pl.pas.hotel.managers;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import pl.pas.hotel.exceptions.*;
import pl.pas.hotel.model.rent.Rent;
import pl.pas.hotel.model.room.Room;
import pl.pas.hotel.model.user.client.Client;
import pl.pas.hotel.repositoriesImplementation.RentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Stateless
public class RentManager {

    @Inject
    private RentRepository rentRepository;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Inject
    private UserManager userManager;

    @Inject
    private RoomManager roomManager;

    public Rent rentRoom(Client client, Room room, LocalDateTime beginTime, LocalDateTime endTime) throws RoomNotAvailable, RentValidationFailed, DateTimeValidationFailed {
        if (validator.validate(client).isEmpty() && validator.validate(room).isEmpty()) {
            if (beginTime.isBefore(endTime)) {
                    final List<Rent> rents = rentRepository.getCurrentRentsByRoom(room.getUuid(), beginTime, endTime);
                    if (rents.isEmpty() && client.isActive()) {
                        return rentRepository.createRent(beginTime, endTime, client, room);
                    } else {
                        throw new RoomNotAvailable("Room is not available");
                    }
            } else {
                throw new DateTimeValidationFailed("Begin time of reservation is not earlier than end time of reservation");
            }
        } else {
            throw new RentValidationFailed("Cannot rent room");
        }
    }

    public void endRoomRent(UUID id) throws RentWithGivenIdNotFound {
        final Rent rent = rentRepository.getRentById(id);
        if (rent != null) {
            rentRepository.endRent(id);
        } else {
            throw new RentWithGivenIdNotFound("Cannot found rent with given id");
        }
    }

    public List<Rent> getRents() {
        return rentRepository.getRents();
    }

    public List<Rent> getRentsByClientId(UUID clientId) throws UserWithGivenIdNotFound {
        userManager.getUserById(clientId);
        return rentRepository.getRentsByClient(clientId);
    }

    public List<Rent> getRentsByRoomId(UUID roomId) throws RoomWithGivenIdNotFound {
        roomManager.getRoomById(roomId);
        return rentRepository.getRentsByRoom(roomId);
    }

    public List<Rent> getRentsByStartDate(LocalDateTime startDate) {
        return rentRepository.getRents().stream().filter(rent -> rent.getBeginTime().isEqual(startDate)).toList();
    }

    public List<Rent> getRentsByEndDate(LocalDateTime endDate) {
        return rentRepository.getRents().stream().filter(rent -> rent.getEndTime().isEqual(endDate)).toList();
    }

    public Rent getRent(UUID id) throws NotFoundException {
        final Rent rent = rentRepository.getRentById(id);
        if (rent == null) {
            throw new NotFoundException("Cannot find rent with given id");
        }
        return rent;
    }

    public void removeRent(UUID rentId) throws RentWithGivenIdNotFound {
        Rent rent = getRent(rentId);
        if(rent.getEndTime().isAfter(LocalDateTime.now())) {
            rentRepository.removeRent(getRent(rentId).getId());
        }
    }

}
