package pl.pas.hotel.repositoriesImplementation;

import jakarta.enterprise.context.ApplicationScoped;
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

    @Override
    public UUID createClient(String personalId, String firstName, String lastName, String address, String login) {
        Client client = new Client(personalId, firstName, lastName, address, login);
        users.add(client);
        return client.getId();
    }

    @Override
    public UUID createAdmin(String login) {
        Admin admin = new Admin(login);
        users.add(admin);
        return admin.getId();
    }

    @Override
    public UUID createManager(String login) {
        Manager manager = new Manager(login);
        users.add(manager);
        return manager.getId();
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
    public User modifyUser(UUID id, String login, String firstName, String lastName, String address) {
        User user = getUserById(id);
        if(user != null) {
            if(user instanceof Client client) {
                client.setFirstName(firstName);
                client.setLastName(lastName);
                client.setAddress(address);
                client.getUser().setLogin(login);
                return client;
            } else if (user instanceof Admin admin) {
                admin.setLogin(login);
            } else if(user instanceof Manager manager) {
                manager.setLogin(login);
            }
        }
        return null;
    }

    @Override
    public User getUserById(UUID id) {
        Optional<User> userOptional = users.stream().filter(user -> user.getId().equals(id)).findFirst();
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
