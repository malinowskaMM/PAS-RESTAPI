package pl.pas.hotel.repositoriesImplementation;

import jakarta.enterprise.context.ApplicationScoped;
import pl.pas.hotel.model.user.AccessLevel;
import pl.pas.hotel.model.user.User;
import pl.pas.hotel.model.user.admin.Admin;
import pl.pas.hotel.model.user.client.Client;
import pl.pas.hotel.model.user.manager.Manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import static java.util.Collections.synchronizedList;

@ApplicationScoped
public class UserRepository implements pl.pas.hotel.repositories.UserRepository {

    private final List<User> users = synchronizedList(new ArrayList<>());

    boolean isLoginUnique(String login) {
        return users.stream().filter(user -> user.getLogin().equals(login)).toList().isEmpty();
    }

    @Override
    public Client createClient(String personalId, String firstName, String lastName, String address, String login, String password, AccessLevel accessLevel) {
        if(isLoginUnique(login)) {
            Client client = new Client(personalId, firstName, lastName, address, login, password, accessLevel);
            users.add(client);
            return client;
        }
        return null;
    }

    @Override
    public Admin createAdmin(String login, String password, AccessLevel accessLevel) {
        if(isLoginUnique(login)) {
            Admin admin = new Admin(login, password, accessLevel);
            users.add(admin);
            return admin;
        }
        return null;
    }

    @Override
    public Manager createManager(String login, String password, AccessLevel accessLevel) {
        if(isLoginUnique(login)) {
            Manager manager = new Manager(login, password, accessLevel);
            users.add(manager);
            return manager;
        }
        return null;
    }

    @Override
    public List<User> getUsers() {
        return users.stream().toList();
    }

    @Override
    public List<User> getUsersBy(Predicate<User> predicate) {
        return getUsers().stream().filter(predicate).toList();
    }

    @Override
    public User modifyUser(UUID id, String login, String password, AccessLevel accessLevel ,String firstName, String lastName, String address) {
        User user = getUserById(id);
        if(user != null) {
            if(user instanceof Client client && accessLevel.getAccessLevel().equals(AccessLevel.CLIENT.name())) {
                client.setFirstName(firstName);
                client.setLastName(lastName);
                client.setAddress(address);
                client.setLogin(login);
                client.setPassword(password);
                client.setAccessLevel(accessLevel);
                return client;
            } else if (user instanceof Admin admin && accessLevel.getAccessLevel().equals(AccessLevel.ADMIN.name())) {
                admin.setLogin(login);
                admin.setPassword(password);
                admin.setAccessLevel(accessLevel);
            } else if(user instanceof Manager manager && accessLevel.getAccessLevel().equals(AccessLevel.MANAGER.name())) {
                manager.setLogin(login);
                manager.setPassword(password);
                manager.setAccessLevel(accessLevel);
            }
        }
        return null;
    }

    @Override
    public User getUserById(UUID id) {
        Optional<User> userOptional = users.stream().filter(user -> user.getUuid().equals(id)).findFirst();
        return userOptional.orElse(null);
    }

    @Override
    public void activateUser(UUID id) {
        User user = getUserById(id);
        if(user != null) {
            user.activate();
        }
    }

    @Override
    public void deactivateUser(UUID id) {
        User user = getUserById(id);
        if(user != null) {
            user.deactivate();
        }
    }

    public void deleteUser(UUID id) {
        users.remove(getUserById(id));
    }
}
