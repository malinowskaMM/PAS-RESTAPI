package pl.pas.hotel.model.user.manager;

import pl.pas.hotel.model.user.AccessLevel;
import pl.pas.hotel.model.user.User;

public class Manager extends User {

    public Manager(String login, String password, AccessLevel accessLevel) {
        super(true, login, password, accessLevel);
    }
}
