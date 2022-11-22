package pl.pas.hotel.model.user.manager;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.pas.hotel.model.user.User;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Manager extends User {

    public Manager(String login) {
        super(true, login);
    }
}
