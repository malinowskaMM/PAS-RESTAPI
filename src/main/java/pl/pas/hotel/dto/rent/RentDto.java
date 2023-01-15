package pl.pas.hotel.dto.rent;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RentDto {

    @NotNull
    String clientId;

    @NotNull
    String roomId;

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
    }

}
