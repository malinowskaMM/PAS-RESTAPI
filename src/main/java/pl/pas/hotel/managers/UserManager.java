package pl.pas.hotel.managers;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import pl.pas.hotel.exceptions.*;
import pl.pas.hotel.model.user.User;
import pl.pas.hotel.model.user.admin.Admin;
import pl.pas.hotel.model.user.client.Client;
import pl.pas.hotel.model.user.manager.Manager;
import pl.pas.hotel.repositoriesImplementation.UserRepository;


import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@Stateless
public class UserManager {

    @Inject
    private UserRepository userRepository;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public synchronized Client registerClient(String firstName, String lastName, String personalId, String address, String login) throws ClientValidationFailed {
        final Client client = new Client(personalId, firstName, lastName, address, login);
        if (validator.validate(client).isEmpty()) {
                    final UUID saved = userRepository.createClient(client.getPersonalId(), client.getFirstName(), client.getLastName(), client.getAddress(), client.getLogin());
                    return (Client) userRepository.getUserById(saved).getUser();
        } else {
            throw new ClientValidationFailed("Cannot register client");
        }
    }

    public synchronized Manager registerManager(String login) throws ManagerValidationFailed {
        final Manager manager = new Manager(login);
        if (validator.validate(manager).isEmpty()) {
                final UUID saved = userRepository.createManager(manager.getLogin());
                return (Manager) userRepository.getUserById(saved).getUser();
        } else {
            throw new ManagerValidationFailed("Cannot register manager");
        }
    }

    public synchronized Admin registerAdmin(String login) throws AdminValidationFailed {
        final Admin admin = new Admin(login);
        if (validator.validate(admin).isEmpty()) {
                final UUID saved = userRepository.createAdmin(admin.getLogin());
                return (Admin) userRepository.getUserById(saved).getUser();
        } else {
            throw new AdminValidationFailed("Cannot register admin");
        }
    }

    public synchronized void updateUser(UUID id, String firstName, String lastName, String address, String login) throws UserWithGivenIdNotFound {
        final User user = userRepository.getUserById(id).getUser();
        if (user == null) {
            throw new UserWithGivenIdNotFound("Not found user with given id");
        } else {
            userRepository.modifyUser(id, login, firstName, lastName, address);
        }
    }

    public Client getClientById(UUID id) throws ClientWithGivenIdNotFound {
            final Client client = (Client) userRepository.getUserById(id).getUser();
            if (client == null) {
                throw new ClientWithGivenIdNotFound("Not found client with given id");
            }
            return client;
    }

    public User getUserById(UUID id) throws UserWithGivenIdNotFound {
        final User user = userRepository.getUserById(id).getUser();
        if (user == null) {
            throw new UserWithGivenIdNotFound("Not found user with given id");
        }
        return user;
    }

    public List<User> findClients(Predicate<User> predicate) {
        return userRepository.getUsersBy(predicate);
    }

    public void activateUser(UUID id) throws UserWithGivenIdNotFound {
        userRepository.activateUser(getUserById(id).getId());
    }

    public void deactivateUser(UUID id) throws UserWithGivenIdNotFound {
        userRepository.deactivateUser(getUserById(id).getId());
    }

    public List<User> getAllUsers(){
        return userRepository.getUsers();
    }

    public void deleteUser(UUID id) throws UserWithGivenIdNotFound {
        userRepository.deleteUser(getUserById(id).getId());
    }

}
