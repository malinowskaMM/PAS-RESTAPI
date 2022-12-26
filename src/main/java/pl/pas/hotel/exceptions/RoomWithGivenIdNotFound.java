package pl.pas.hotel.exceptions;

public class RoomWithGivenIdNotFound extends Exception{
    public RoomWithGivenIdNotFound(String message) {
        super(message);
    }
}
