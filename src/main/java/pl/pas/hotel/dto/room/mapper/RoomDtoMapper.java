package pl.pas.hotel.dto.room.mapper;

import lombok.NoArgsConstructor;
import pl.pas.hotel.dto.room.RoomDto;
import pl.pas.hotel.model.room.Room;


@NoArgsConstructor
public class RoomDtoMapper {

    public Room toRoom(RoomDto roomDto) {
        return Room.builder()
                .roomNumber(roomDto.getRoomNumber())
                .price(roomDto.getPrice())
                .roomCapacity(roomDto.getRoomCapacity())
                .build();
    }

}
