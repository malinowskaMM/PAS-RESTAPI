package pl.pas.hotel.model.room;

import lombok.*;
import pl.pas.hotel.model.abstractEntity.AbstractEntity;

import java.util.UUID;

@Builder
@Getter
public class Room extends AbstractEntity {

    private UUID roomId;
    private Integer roomNumber;
    private Double price;
    private Integer roomCapacity;

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setRoomCapacity(Integer roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public Room(Integer roomNumber, Double price, Integer roomCapacity) {
        this.roomId = UUID.randomUUID();
        this.roomNumber = roomNumber;
        this.price = price;
        this.roomCapacity = roomCapacity;
    }
}
