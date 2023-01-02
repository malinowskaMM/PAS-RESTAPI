package pl.pas.hotel.exceptions;

import jakarta.ws.rs.NotFoundException;

public class RoomWithGivenIdNotFound extends NotFoundException {
    public RoomWithGivenIdNotFound(String message) {
        super(message);
    }
}
