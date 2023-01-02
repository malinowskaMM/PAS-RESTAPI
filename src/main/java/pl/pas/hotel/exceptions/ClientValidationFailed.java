package pl.pas.hotel.exceptions;

import jakarta.validation.ValidationException;

public class ClientValidationFailed extends ValidationException {
    public ClientValidationFailed(String message) {
        super(message);
    }
}
