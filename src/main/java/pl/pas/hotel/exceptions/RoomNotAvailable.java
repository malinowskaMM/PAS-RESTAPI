package pl.pas.hotel.exceptions;

import jakarta.ws.rs.NotAllowedException;

public class RoomNotAvailable extends NotAllowedException {
    public RoomNotAvailable(String message) {
        super(message);
    }
}
