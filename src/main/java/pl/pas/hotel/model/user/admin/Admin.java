package pl.pas.hotel.model.user.admin;

import pl.pas.hotel.model.user.User;

public class Admin extends User {

    public Admin(String login) {
        super(true, login);
    }

    public Admin getUser() {
        return this;
    }
}
