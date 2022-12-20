package pl.pas.hotel.dto.rent;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class RentDto {

    String clientId;

    UUID roomUuid;

    LocalDateTime beginTime;

    LocalDateTime endTime;

    @JsonbCreator
    public RentDto(@JsonbProperty("clientId")String clientId, @JsonbProperty("roomUuid")UUID roomUuid, @JsonbProperty("startDate") LocalDateTime beginTime, @JsonbProperty("endDate") LocalDateTime endTime) {
        this.clientId = clientId;
        this.roomUuid = roomUuid;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }
}
