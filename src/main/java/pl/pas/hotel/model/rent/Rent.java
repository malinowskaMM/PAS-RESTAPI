package pl.pas.hotel.model.rent;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import pl.pas.hotel.model.abstractEntity.AbstractEntity;
import pl.pas.hotel.model.room.Room;
import pl.pas.hotel.model.user.client.Client;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Rent extends AbstractEntity {
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
