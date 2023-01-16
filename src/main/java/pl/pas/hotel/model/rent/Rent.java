package pl.pas.hotel.model.rent;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Rent implements Serializable {
    UUID id;
    LocalDateTime beginTime;
    LocalDateTime endTime;
    String login;
    String roomId;

    public Rent(LocalDateTime beginTime, LocalDateTime endTime, String login, String roomId) {
        this.id = UUID.randomUUID();
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.login = login;
        this.roomId = roomId;
    }

    public void endRent() {
        this.endTime = LocalDateTime.now();
    }
}
