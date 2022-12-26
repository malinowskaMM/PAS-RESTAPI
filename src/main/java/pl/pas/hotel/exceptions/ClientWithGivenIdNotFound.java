package pl.pas.hotel.exceptions;

public class ClientWithGivenIdNotFound extends Exception{
    public ClientWithGivenIdNotFound(String message) {
        super(message);
    }
}
