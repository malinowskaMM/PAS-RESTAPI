package pl.pas.hotel.exceptions;

import jakarta.ws.rs.NotFoundException;

public class RentWithGivenIdNotFound extends NotFoundException {
    public RentWithGivenIdNotFound(String message) {
        super(message);
    }
}
