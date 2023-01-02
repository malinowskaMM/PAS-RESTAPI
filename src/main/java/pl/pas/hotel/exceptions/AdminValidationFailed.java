package pl.pas.hotel.exceptions;

import jakarta.validation.ValidationException;

public class AdminValidationFailed extends ValidationException {
    public AdminValidationFailed(String message) {
        super(message);
    }
}
