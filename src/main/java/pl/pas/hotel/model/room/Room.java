package pl.pas.hotel.model.room;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class Room implements Serializable {

    private UUID uuid;
    private Integer roomNumber;
    private Double price;
    private Integer roomCapacity;

    private boolean isRented;

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setRoomCapacity(Integer roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public void setRented(boolean rented) {
        isRented = rented;
    }

    public Room(Integer roomNumber, Double price, Integer roomCapacity) {
        this.uuid = UUID.randomUUID();
        this.roomNumber = roomNumber;
        this.price = price;
        this.roomCapacity = roomCapacity;
        this.isRented = false;
    }
}
