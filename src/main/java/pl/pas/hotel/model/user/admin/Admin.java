package pl.pas.hotel.model.user.admin;

import pl.pas.hotel.model.user.AccessLevel;
import pl.pas.hotel.model.user.User;

public class Admin extends User {

    public Admin(String login, String password, AccessLevel accessLevel) {
        super(true, login, password, accessLevel);
    }

}
