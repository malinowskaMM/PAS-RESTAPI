package pl.pas.hotel.exceptions;

public class RentValidationFailed extends Exception{
    public RentValidationFailed(String message) {
        super(message);
    }
}
