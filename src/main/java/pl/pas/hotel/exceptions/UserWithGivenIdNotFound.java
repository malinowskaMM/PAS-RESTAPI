package pl.pas.hotel.exceptions;

public class UserWithGivenIdNotFound extends Exception{
    public UserWithGivenIdNotFound(String message) {
        super(message);
    }
}
