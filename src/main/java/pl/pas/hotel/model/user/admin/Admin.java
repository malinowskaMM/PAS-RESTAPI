package pl.pas.hotel.model.user.admin;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import pl.pas.hotel.model.user.User;

@AllArgsConstructor
public class Admin extends User {

    public Admin(String login) {
        super(true, login);
    }

    public Admin getUser() {
        return this;
    }
}
