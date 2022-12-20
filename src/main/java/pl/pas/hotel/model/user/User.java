package pl.pas.hotel.model.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import pl.pas.hotel.model.abstractEntity.AbstractEntity;

import java.util.UUID;

@Entity
@Getter
public abstract class User extends AbstractEntity {

    @Id
    private UUID id;
    private boolean isActive;
    private final String login;

    protected User(boolean isActive, String login) {
        this.id = UUID.randomUUID();
        this.isActive = isActive;
        this.login = login;
    }

    protected User() {
        this.id = null;
        this.login = null;
        this.isActive = false;
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

    public abstract User getUser();
}
