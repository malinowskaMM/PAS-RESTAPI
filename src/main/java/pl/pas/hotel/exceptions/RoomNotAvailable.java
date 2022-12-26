package pl.pas.hotel.exceptions;

public class RoomNotAvailable extends Exception{
    public RoomNotAvailable(String message) {
        super(message);
    }
}
