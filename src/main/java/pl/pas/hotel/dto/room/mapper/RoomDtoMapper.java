package pl.pas.hotel.dto.room.mapper;

import jakarta.ejb.Stateless;
import lombok.NoArgsConstructor;
import pl.pas.hotel.dto.room.RoomDto;
import pl.pas.hotel.model.room.Room;


@NoArgsConstructor
@Stateless
public class RoomDtoMapper {

    public Room toRoom(RoomDto roomDto) {
        return new Room(roomDto.getRoomNumber(), roomDto.getPrice(), roomDto.getRoomCapacity());
    }

}
