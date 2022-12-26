package pl.pas.hotel.exceptions;

public class RoomWithGivenIdNotExist extends Exception{
    public RoomWithGivenIdNotExist(String message) {
        super(message);
    }
}
