package pl.pas.hotel.repositories;


import pl.pas.hotel.model.user.User;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public interface UserRepository {

    UUID createClient(String personalId, String firstName, String lastName, Address address, String login);
    UUID createAdmin(String login);
    UUID createManager(String login);
    List<User> getUsers();
    List<User> getUsersBy(Predicate<User> predicate);
    User modifyClient(UUID id, String firstName, String lastName, Address address);
    User getUserById(UUID id);
    void activateUser(UUID id);
    void deactivateUser(UUID id);
}
