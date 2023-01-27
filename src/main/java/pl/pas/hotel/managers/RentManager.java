package pl.pas.hotel.managers;

import com.nimbusds.jose.JOSEException;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;
import org.json.simple.JSONObject;
import pl.pas.hotel.auth.JwsGenerator;
import pl.pas.hotel.exceptions.*;
import pl.pas.hotel.model.rent.Rent;
import pl.pas.hotel.model.room.Room;
import pl.pas.hotel.model.user.User;
import pl.pas.hotel.repositoriesImplementation.RentRepository;

import java.text.ParseException;
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

    private JwsGenerator jwsGenerator = new JwsGenerator();

    public Rent rentRoom(String roomId, LocalDateTime beginTime, LocalDateTime endTime) throws RoomNotAvailable, RentValidationFailed, DateTimeValidationFailed {
        User user = userManager.getUserFromServerContext();
        if (user != null && validator.validate(roomId).isEmpty()) {
            if (beginTime.isBefore(endTime)) {
                    final List<Rent> rents = rentRepository.getCurrentRentsByRoom(UUID.fromString(roomId), beginTime, endTime);
                    if (rents.isEmpty() && userManager.getUserByIdInside(user.getUuid()).isActive()) {
                        return rentRepository.createRent(beginTime, endTime, user.getLogin(), roomId);
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

    public void endRoomRent(UUID id, String jws) throws RentWithGivenIdNotFound, ParseException, JOSEException {
        final Rent rent = rentRepository.getRentById(id);
        if(rent != null) {
            if (this.jwsGenerator.verify(jws)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", rent.getId().toString());
                String newJwt = this.jwsGenerator.generateJws(jsonObject.toString());
                if (newJwt.equals(jws)) {
                    rentRepository.endRent(id);
                }
            }
        }
        throw new RentWithGivenIdNotFound("Cannot find room with given id.");
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

    public List<Rent> getPastRentsByClientId(String login) {
        userManager.findClientsByLoginPart(login);
        return getPastRents().stream().filter(rent -> rent.getLogin().equals(login)).toList();
    }

    public List<Rent> getCurrentRentsByClientId(String login) {
        userManager.findClientsByLoginPart(login);
        return getCurrentRents().stream().filter(rent -> rent.getLogin().equals(login)).toList();
    }

    public List<Rent> getPastRentsByRoomId(UUID roomId) {
        roomManager.getRoomById(roomId);
        return getPastRents().stream().filter(rent -> rent.getRoomId().equals(roomId.toString())).toList();
    }

    public List<Room> getCurrentFreeRooms() {
        return roomManager.getAllRooms().stream().filter(room -> getCurrentRentsByRoomId(room.getUuid()).isEmpty()).toList();
    }

    public List<Rent> getCurrentRentsByRoomId(UUID roomId) {
        roomManager.getRoomById(roomId);
        return getCurrentRents().stream().filter(rent -> rent.getRoomId().equals(roomId.toString())).toList();
    }

    public List<Rent> getRentsByClientId(String login) throws UserWithGivenIdNotFound {
        userManager.findClientsByLoginPart(login);
        return rentRepository.getRentsByClient(login);
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
