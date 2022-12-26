package pl.pas.hotel.model.user;

import jakarta.json.bind.annotation.JsonbTransient;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class User implements Serializable {

    private UUID id;
    private boolean isActive;
    private String login;

    protected User(boolean isActive, String login) {
        this.id = UUID.randomUUID();
        this.isActive = isActive;
        this.login = login;
    }

    public boolean isActive() {
        return isActive;
    }

    public void activate() {
        isActive = true;
    }

    public void deactivate() {
        isActive = false;
    }

    public String getLogin() {
        return login;
    }

    public User getUser(){
        return this;
    }
}
