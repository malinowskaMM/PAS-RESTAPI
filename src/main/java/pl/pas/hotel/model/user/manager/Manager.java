package pl.pas.hotel.model.user.manager;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import pl.pas.hotel.model.user.User;

@Builder
public class Manager extends User {

    public Manager(String login) {
        super(true, login);
    }
    public Manager getUser() {
        return this;
    }
}
