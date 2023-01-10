package pl.pas.hotel.managers;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;
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

    public Rent rentRoom(String clientId, String roomId, LocalDateTime beginTime, LocalDateTime endTime) throws RoomNotAvailable, RentValidationFailed, DateTimeValidationFailed {
        if (validator.validate(clientId).isEmpty() && validator.validate(roomId).isEmpty()) {
            if (beginTime.isBefore(endTime)) {
                    final List<Rent> rents = rentRepository.getCurrentRentsByRoom(UUID.fromString(roomId), beginTime, endTime);
                    if (rents.isEmpty() && userManager.getUserById(UUID.fromString(clientId)).isActive()) {
                        return rentRepository.createRent(beginTime, endTime, clientId, roomId);
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

    public List<Rent> getPastRents() {
        return getRents().stream().filter(rent -> (rent.getBeginTime().isBefore(LocalDateTime.now()) && rent.getEndTime().isBefore(LocalDateTime.now()))).toList();
    }

    public List<Rent> getCurrentRents() {
        return getRents().stream().filter(rent -> (rent.getBeginTime().isBefore(LocalDateTime.now()) && rent.getEndTime().isAfter(LocalDateTime.now()))).toList();
    }

    public List<Rent> getPastRentsByClientId(UUID clientId) {
        userManager.getUserById(clientId);
        return getPastRents().stream().filter(rent -> rent.getClientId().equals(clientId.toString())).toList();
    }

    public List<Rent> getCurrentRentsByClientId(UUID clientId) {
        userManager.getUserById(clientId);
        return getCurrentRents().stream().filter(rent -> rent.getClientId().equals(clientId.toString())).toList();
    }

    public List<Rent> getPastRentsByRoomId(UUID roomId) {
        roomManager.getRoomById(roomId);
        return getPastRents().stream().filter(rent -> rent.getRoomId().equals(roomId.toString())).toList();
    }

    public List<Rent> getCurrentRentsByRoomId(UUID roomId) {
        roomManager.getRoomById(roomId);
        return getCurrentRents().stream().filter(rent -> rent.getRoomId().equals(roomId.toString())).toList();
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
