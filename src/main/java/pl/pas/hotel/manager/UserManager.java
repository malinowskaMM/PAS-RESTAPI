package pl.pas.hotel.manager;

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
import pl.pas.hotel.repositoryImpl.UserRepository;


import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class UserManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(Slf4j.class);
    @PersistenceContext
    private final EntityManager entityManager;
    private final UserRepository userRepository;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public UserManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.userRepository = new UserRepository(entityManager);
    }

    public synchronized Client registerClient(String firstName, String lastName, String personalId, Address address, String login) {
        final Client client = new Client(personalId, firstName, lastName, address, login);
        if (validator.validate(client).size() == 0) {
            try {
                entityManager.getTransaction().begin();
                    final UUID saved = userRepository.createClient(client.getPersonalId(), client.getFirstName(), client.getLastName(), client.getAddress(), client.getLogin());
                    entityManager.getTransaction().commit();
                    LOGGER.debug("Client registered successfully");
                    return userRepository.getClientById(saved);
            } catch (RollbackException e) {
                LOGGER.error("Transaction failed:", e);
                entityManager.getTransaction().rollback();
            }
        } else {
            LOGGER.error("Client {} validation failed.", client.getId());
        }
        return null;
    }

    public synchronized Manager registerManager(String login) {
        final Manager manager = new Manager(login);
        if (validator.validate(manager).size() == 0) {
            try {
                entityManager.getTransaction().begin();
                final UUID saved = userRepository.createManager(manager.getLogin());
                entityManager.getTransaction().commit();
                LOGGER.debug("Manager registered successfully");
                return userRepository.getManagerById(saved);
            } catch (RollbackException e) {
                LOGGER.error("Transaction failed:", e);
                entityManager.getTransaction().rollback();
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
                entityManager.getTransaction().begin();
                final UUID saved = userRepository.createAdmin(admin.getLogin());
                entityManager.getTransaction().commit();
                LOGGER.debug("Admin registered successfully");
                return userRepository.getAdminById(saved);
            } catch (RollbackException e) {
                LOGGER.error("Transaction failed:", e);
                entityManager.getTransaction().rollback();
            }
        } else {
            LOGGER.error("Admin {} validation failed.", admin.getId());
        }
        return null;
    }

    public synchronized void updateClient(UUID id, String firstName, String lastName, String personalId, Address address, String login) {
        entityManager.getTransaction().begin();
        final Client client = userRepository.getClientById(id);
        if (client == null) {
            LOGGER.warn("Client {} does not exist in the database", id);
            entityManager.getTransaction().rollback();
        } else {
            userRepository.modifyClient(id, firstName, lastName, address);
            if (!validator.validate(userRepository.getUserById(id)).isEmpty()) {
                LOGGER.warn("Room {} validation failed", id);
                entityManager.getTransaction().rollback();
            }
            entityManager.getTransaction().commit();
            LOGGER.debug("Room modified successfully");
        }
    }

    public Client getClientById(UUID id) {
            return userRepository.getClientById(id);
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
