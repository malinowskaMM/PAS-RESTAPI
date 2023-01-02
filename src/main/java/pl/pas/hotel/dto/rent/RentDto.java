package pl.pas.hotel.dto.rent;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class RentDto {

    @NotNull
    String clientId;

    @NotNull
    String roomId;

    @AssertTrue
    private boolean isEndTimeAfterBeginTime() {
        return endTime.isAfter(beginTime);
    }

    @FutureOrPresent
    @NotNull
    LocalDateTime beginTime;

    @FutureOrPresent
    @NotNull
    LocalDateTime endTime;

    @JsonbCreator
    public RentDto(@JsonbProperty("clientId")String clientId, @JsonbProperty("roomId")String roomId, @JsonbProperty("startDate") LocalDateTime beginTime, @JsonbProperty("endDate") LocalDateTime endTime) {
        isEndTimeAfterBeginTime();
        this.clientId = clientId;
        this.roomId = roomId;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }
}
