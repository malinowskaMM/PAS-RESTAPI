package pl.pas.hotel.model.user.admin;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import pl.pas.hotel.model.user.User;

@Builder
public class Admin extends User {

    public Admin(String login) {
        super(true, login);
    }

    public Admin getUser() {
        return this;
    }
}
