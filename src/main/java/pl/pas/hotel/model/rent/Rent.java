package pl.pas.hotel.model.rent;

import lombok.*;
import pl.pas.hotel.model.room.Room;
import pl.pas.hotel.model.user.client.Client;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Rent implements Serializable {
    UUID id;
    LocalDateTime beginTime;
    LocalDateTime endTime;
    Client client;
    Room room;

    public Rent(LocalDateTime beginTime, LocalDateTime endTime, Client client, Room room) {
        this.id = UUID.randomUUID();
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.client = client;
        this.room = room;
    }

    public void endRent() {
        this.endTime = LocalDateTime.now();
    }
}
