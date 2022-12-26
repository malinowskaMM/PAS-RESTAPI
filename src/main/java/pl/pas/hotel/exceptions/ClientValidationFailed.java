package pl.pas.hotel.exceptions;

public class ClientValidationFailed extends Exception{
    public ClientValidationFailed(String message) {
        super(message);
    }
}
