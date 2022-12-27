package pl.pas.hotel.dto.rent.mapper;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import pl.pas.hotel.dto.rent.RentDto;
import pl.pas.hotel.dto.user.mapper.UserDtoMapper;
import pl.pas.hotel.exceptions.ClientWithGivenIdNotFound;
import pl.pas.hotel.exceptions.RoomWithGivenIdNotFound;
import pl.pas.hotel.managers.RoomManager;
import pl.pas.hotel.managers.UserManager;
import pl.pas.hotel.model.rent.Rent;
import pl.pas.hotel.model.room.Room;
import pl.pas.hotel.model.user.client.Client;


@NoArgsConstructor
@Stateless
public class RentDtoMapper {

    @Inject
    UserManager userManager;
    @Inject
    RoomManager roomManager;


    public Rent toRent(RentDto rentDto) throws RoomWithGivenIdNotFound, ClientWithGivenIdNotFound {
        Client client = userManager.getClientById(rentDto.getClientId());
        Room room = roomManager.getRoomById(rentDto.getRoomId());

        return new Rent(rentDto.getBeginTime(), rentDto.getEndTime(), client, room);
    }

}
