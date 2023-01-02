package pl.pas.hotel.exceptions;

import jakarta.validation.ValidationException;

public class RentValidationFailed extends ValidationException {
    public RentValidationFailed(String message) {
        super(message);
    }
}
