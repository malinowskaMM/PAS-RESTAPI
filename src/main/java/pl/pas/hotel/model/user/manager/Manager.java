package pl.pas.hotel.model.user.manager;

import pl.pas.hotel.model.user.User;

public class Manager extends User {

    public Manager(String login) {
        super(true, login);
    }
    public Manager getUser() {
        return this;
    }
}
