package pl.pas.hotel.model.room;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import pl.pas.hotel.model.abstractEntity.AbstractEntity;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@jakarta.enterprise.context.Dependent
public class Room extends AbstractEntity {

    @Id
    private UUID roomId;

    @Positive
    @Column(name = "ROOM_NUMBER", columnDefinition = "INTEGER CHECK (ROOM_NUMBER > 0)")
    private Integer roomNumber;

    @NotNull
    @PositiveOrZero
    @Column(name = "PRICE", nullable = false, columnDefinition = "DOUBLE PRECISION CHECK (PRICE >= 0)")
    private Double price;

    @NotNull
    @Positive
    @Column(name = "ROOM_CAPACITY", nullable = false, columnDefinition = "INTEGER CHECK (ROOM_CAPACITY > 0)")
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
