package pl.pas.hotel.exceptions;

import jakarta.validation.ValidationException;

public class DateTimeValidationFailed extends ValidationException {
    public DateTimeValidationFailed(String message) {
        super(message);
    }
}
