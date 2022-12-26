package pl.pas.hotel.repositories;


import pl.pas.hotel.model.user.AccessLevel;
import pl.pas.hotel.model.user.User;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public interface UserRepository {

    UUID createClient(String personalId, String firstName, String lastName, String address, String login, String password, AccessLevel accessLevel);
    UUID createAdmin(String login, String password, AccessLevel accessLevel);
    UUID createManager(String login, String password, AccessLevel accessLevel);
    List<User> getUsers();
    List<User> getUsersBy(Predicate<User> predicate);
    User modifyUser(UUID id, String login, String password, AccessLevel accessLevel ,String firstName, String lastName, String address);
    User getUserById(UUID id);
    void activateUser(UUID id);
    void deactivateUser(UUID id);
}
