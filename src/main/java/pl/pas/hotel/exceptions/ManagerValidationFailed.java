package pl.pas.hotel.exceptions;

import jakarta.validation.ValidationException;

public class ManagerValidationFailed extends ValidationException {
    public ManagerValidationFailed(String message) {
        super(message);
    }
}
