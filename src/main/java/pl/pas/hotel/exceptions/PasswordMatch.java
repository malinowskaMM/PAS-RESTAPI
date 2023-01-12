package pl.pas.hotel.exceptions;

import jakarta.validation.ValidationException;

public class PasswordMatch extends ValidationException {
    public PasswordMatch(String message) {
        super(message);
    }
}
