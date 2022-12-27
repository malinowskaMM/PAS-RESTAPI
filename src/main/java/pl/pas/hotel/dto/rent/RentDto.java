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
    @Size(min = 11, max = 11)
    String clientId;

    @NotNull
    UUID roomUuid;

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
    public RentDto(@JsonbProperty("clientId")String clientId, @JsonbProperty("roomUuid")UUID roomUuid, @JsonbProperty("startDate") LocalDateTime beginTime, @JsonbProperty("endDate") LocalDateTime endTime) {
        this.clientId = clientId;
        this.roomUuid = roomUuid;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }
}
