package pl.pas.hotel.managers;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.RollbackException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.pas.hotel.model.user.User;
import pl.pas.hotel.model.user.admin.Admin;
import pl.pas.hotel.model.user.client.Address;
import pl.pas.hotel.model.user.client.Client;
import pl.pas.hotel.model.user.manager.Manager;
import pl.pas.hotel.repositoriesImplementation.UserRepository;


import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@Stateless
public class UserManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(Slf4j.class);

    private final UserRepository userRepository;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Inject
    public UserManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public synchronized Client registerClient(String firstName, String lastName, String personalId, Address address, String login) {
        final Client client = new Client(personalId, firstName, lastName, address, login);
        if (validator.validate(client).size() == 0) {
                    final UUID saved = userRepository.createClient(client.getPersonalId(), client.getFirstName(), client.getLastName(), client.getAddress(), client.getLogin());
                    LOGGER.debug("Client registered successfully");
                    return (Client) userRepository.getUserById(saved).getUser();
        } else {
            LOGGER.error("Client {} validation failed.", client.getId());
        }
        return null;
    }

    public synchronized Manager registerManager(String login) {
        final Manager manager = new Manager(login);
        if (validator.validate(manager).size() == 0) {
            try {
                final UUID saved = userRepository.createManager(manager.getLogin());
                LOGGER.debug("Manager registered successfully");
                return (Manager) userRepository.getUserById(saved).getUser();
            } catch (RollbackException e) {
                LOGGER.error("Transaction failed:", e);
            }
        } else {
            LOGGER.error("Manager {} validation failed.", manager.getId());
        }
        return null;
    }

    public synchronized Admin registerAdmin(String login) {
        final Admin admin = new Admin(login);
        if (validator.validate(admin).size() == 0) {
            try {
                final UUID saved = userRepository.createAdmin(admin.getLogin());
                LOGGER.debug("Admin registered successfully");
                return (Admin) userRepository.getUserById(saved).getUser();
            } catch (RollbackException e) {
                LOGGER.error("Transaction failed:", e);
            }
        } else {
            LOGGER.error("Admin {} validation failed.", admin.getId());
        }
        return null;
    }

    public synchronized void updateClient(UUID id, String firstName, String lastName, String personalId, Address address, String login) {
        final Client client = (Client) userRepository.getUserById(id).getUser();
        if (client == null) {
            LOGGER.warn("Client {} does not exist in the database", id);
        } else {
            userRepository.modifyClient(id, firstName, lastName, address);
            if (!validator.validate(userRepository.getUserById(id)).isEmpty()) {
                LOGGER.warn("Room {} validation failed", id);
            }
            LOGGER.debug("Room modified successfully");
        }
    }

    public Client getClientById(UUID id) {
            return (Client) userRepository.getUserById(id).getUser();
    }

    public User getUserById(UUID id) {
        return userRepository.getUserById(id);
    }

    public List<User> findClients(Predicate<User> predicate) {
        return userRepository.getUsersBy(predicate);
    }

    public void activateUser(UUID id) {
        userRepository.activateUser(id);
    }

    public void deactivateUser(UUID id) {
        userRepository.deactivateUser(id);
    }

    public List<User> getAllUsers(){
        return userRepository.getUsers();
    }

}
