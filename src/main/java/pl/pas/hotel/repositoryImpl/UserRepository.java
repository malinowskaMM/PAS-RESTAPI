package pl.pas.hotel.repositoryImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import pl.pas.hotel.model.user.User;
import pl.pas.hotel.model.user.admin.Admin;
import pl.pas.hotel.model.user.client.Address;
import pl.pas.hotel.model.user.client.Client;
import pl.pas.hotel.model.user.manager.Manager;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class UserRepository implements pl.pas.hotel.repository.UserRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public UUID createClient(String personalId, String firstName, String lastName, Address address, String login) {
        Client client = new Client(personalId, firstName, lastName, address, login);
        entityManager.persist(client);
        return client.getId();
    }

    @Override
    public UUID createAdmin(String login) {
        Admin admin = new Admin(login);
        return admin.getId();
    }

    @Override
    public UUID createManager(String login) {
        Manager manager = new Manager(login);
        return manager.getId();
    }

    @Override
    public List<User> getUsers() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    public List<User> getUsersBy(Predicate<User> predicate) {
        return getUsers().stream().filter(predicate).toList();
    }

    @Override
    public User modifyClient(UUID id, String firstName, String lastName, Address address) {
        Client client = entityManager.find(Client.class, id);
        if(client != null) {
             client.setFirstName(firstName);
             client.setLastName(lastName);
             client.setAddress(address);
             entityManager.merge(client);
             return client;
        }
        return null;
    }

    @Override
    public User getUserById(UUID id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public Client getClientById(UUID id) {
        return entityManager.find(Client.class, id);
    }

    @Override
    public Manager getManagerById(UUID id) {
        return entityManager.find(Manager.class, id);
    }

    @Override
    public Admin getAdminById(UUID id) {
        return entityManager.find(Admin.class, id);
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
}
