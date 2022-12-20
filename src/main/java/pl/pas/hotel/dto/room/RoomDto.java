package pl.pas.hotel.dto.room;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoomDto {

    @Positive
    @Column(name = "roomNumber", columnDefinition = "INTEGER CHECK (ROOM_NUMBER > 0)")
    private Integer roomNumber;

    @NotNull
    @PositiveOrZero
    @Column(name = "price", nullable = false, columnDefinition = "DOUBLE PRECISION CHECK (PRICE >= 0)")
    private Double price;

    @NotNull
    @Positive
    @Column(name = "roomCapacity", nullable = false, columnDefinition = "INTEGER CHECK (ROOM_CAPACITY > 0)")
    private Integer roomCapacity;

    @JsonbCreator
    public RoomDto(@JsonbProperty("roomNumber")Integer roomNumber, @JsonbProperty("price")Double price, @JsonbProperty("roomCapacity")Integer roomCapacity) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.roomCapacity = roomCapacity;
    }
}
