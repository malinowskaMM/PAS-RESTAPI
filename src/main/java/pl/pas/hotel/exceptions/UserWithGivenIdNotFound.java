package pl.pas.hotel.exceptions;

import jakarta.ws.rs.NotFoundException;

public class UserWithGivenIdNotFound extends NotFoundException {
    public UserWithGivenIdNotFound(String message) {
        super(message);
    }
}
