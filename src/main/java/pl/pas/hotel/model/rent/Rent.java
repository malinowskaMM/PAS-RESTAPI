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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@jakarta.enterprise.context.Dependent
public class Rent extends AbstractEntity {

    @Id
    @NotNull
    UUID id;

    @NotNull
    @Column(name = "BEGIN_TIME", nullable = false, columnDefinition = "TIMESTAMP CHECK (END_TIME > BEGIN_TIME)")
    LocalDateTime beginTime;

    @NotNull
    @Column(name = "END_TIME", nullable = false, columnDefinition = "TIMESTAMP CHECK (END_TIME > BEGIN_TIME)")
    LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "PERSONAL_ID", nullable = false)
    Client client;

    @ManyToOne
    @JoinColumn(name = "ROOM_NUMBER", nullable = false)
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
