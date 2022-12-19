package pl.pas.hotel.model.user.admin;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.pas.hotel.model.user.User;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends User {

    public Admin(String login) {
        super(true, login);
    }

    public Admin getUser() {
        return this;
    }
}
