package pl.pas.hotel.exceptions;

public class RoomValidationFailed extends Exception{
    public RoomValidationFailed(String message) {
        super(message);
    }
}
