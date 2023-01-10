package pl.pas.hotel.dto.rent;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import pl.pas.hotel.dto.room.RoomDto;
import pl.pas.hotel.dto.user.ClientDto;
import pl.pas.hotel.model.room.Room;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class RentDto {

    @NotNull
    String clientId;

    @NotNull
    String roomId;

    // @NotNull
    // ClientDto client;
    //
    // @NotNull
    // RoomDto room;


    // @AssertTrue
    // private boolean isEndTimeAfterBeginTime() {
    //     return this.endTime.isAfter(this.beginTime);
    // }

    @NotNull
    LocalDateTime beginTime;

    @NotNull
    LocalDateTime endTime;

    @JsonbCreator
    public RentDto(@JsonbProperty("clientId")String clientId, @JsonbProperty("roomId")String roomId, @JsonbProperty("startDate") LocalDateTime beginTime, @JsonbProperty("endDate") LocalDateTime endTime) {
        this.clientId = clientId;
        this.roomId = roomId;
        this.beginTime = beginTime;
        this.endTime = endTime;
        // isEndTimeAfterBeginTime();
    }

    // @JsonbCreator
    // public RentDto(@JsonbProperty("client") ClientDto client, @JsonbProperty("room")RoomDto room, @JsonbProperty("startDate") LocalDateTime beginTime, @JsonbProperty("endDate") LocalDateTime endTime) {
    //     this.client = client;
    //     this.room = room;
    //     this.beginTime = beginTime;
    //     this.endTime = endTime;
    //     // isEndTimeAfterBeginTime();
    // }
}
