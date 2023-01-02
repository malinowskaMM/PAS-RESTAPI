package pl.pas.hotel.exceptions;

import jakarta.ws.rs.NotFoundException;

public class ClientWithGivenIdNotFound extends NotFoundException {
    public ClientWithGivenIdNotFound(String message) {
        super(message);
    }
}
