package pl.pas.hotel.dto.rent.mapper;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import pl.pas.hotel.dto.rent.RentDto;
import pl.pas.hotel.managers.RoomManager;
import pl.pas.hotel.managers.UserManager;
import pl.pas.hotel.model.rent.Rent;
import pl.pas.hotel.model.room.Room;
import pl.pas.hotel.model.user.client.Client;

import java.util.UUID;

@NoArgsConstructor
@Stateless
public class RentDtoMapper {

    @Inject
    UserManager userManager;

    @Inject
    RoomManager roomManager;

    public Rent toRent(RentDto rentDto) {
        Client client = userManager.getClientById(UUID.fromString(rentDto.getClientId()));
        Room room = roomManager.getRoomById(rentDto.getRoomUuid().toString());

        return Rent.builder()
                .beginTime(rentDto.getBeginTime())
                .endTime(rentDto.getEndTime())
                .client(client)
                .room(room)
                .build();
    }

}
