package pl.pas.hotel.exceptions;

import jakarta.validation.ValidationException;

public class PasswordMatchFailed extends ValidationException {
    public PasswordMatchFailed(String message) {
        super(message);
    }
}
