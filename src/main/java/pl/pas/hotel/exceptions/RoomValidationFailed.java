package pl.pas.hotel.exceptions;

import jakarta.validation.ValidationException;

public class RoomValidationFailed extends ValidationException {
    public RoomValidationFailed(String message) {
        super(message);
    }
}
