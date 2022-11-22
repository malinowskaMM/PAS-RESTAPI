package pl.pas.hotel.repository;


import pl.pas.hotel.model.user.User;
import pl.pas.hotel.model.user.admin.Admin;
import pl.pas.hotel.model.user.client.Address;
import pl.pas.hotel.model.user.client.Client;
import pl.pas.hotel.model.user.manager.Manager;

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
    Client getClientById(UUID id);
    Manager getManagerById(UUID id);
    Admin getAdminById(UUID id);
    void activateUser(UUID id);
    void deactivateUser(UUID id);
}
