package pl.pas.hotel.managers;

import com.nimbusds.jose.JOSEException;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import org.json.simple.JSONObject;
import pl.pas.hotel.auth.JwsGenerator;
import pl.pas.hotel.exceptions.*;
import pl.pas.hotel.model.user.AccessLevel;
import pl.pas.hotel.model.user.User;
import pl.pas.hotel.model.user.admin.Admin;
import pl.pas.hotel.model.user.client.Client;
import pl.pas.hotel.model.user.manager.Manager;
import pl.pas.hotel.repositoriesImplementation.UserRepository;


import java.text.ParseException;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@Stateless
public class UserManager {

    @Inject
    private UserRepository userRepository;

    @Context
    private SecurityContext securityContext;

    private JwsGenerator jwsGenerator = new JwsGenerator();

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public synchronized Client registerClient(String firstName, String lastName, String personalId, String address, String login, String password) throws ClientValidationFailed {
        final Client client = new Client(personalId, firstName, lastName, address, login, password, AccessLevel.CLIENT);
        if (validator.validate(client).isEmpty()) {
                    return userRepository.createClient(client.getPersonalId(), client.getFirstName(), client.getLastName(), client.getAddress(), client.getLogin(), client.getPassword(), client.getAccessLevel());
        } else {
            throw new ClientValidationFailed("Cannot register client");
        }
    }

    public synchronized Manager registerManager(String login, String password) throws ManagerValidationFailed {
        final Manager manager = new Manager(login, password, AccessLevel.MANAGER);
        if (validator.validate(manager).isEmpty()) {
                return userRepository.createManager(manager.getLogin(), manager.getPassword(), manager.getAccessLevel());
        } else {
            throw new ManagerValidationFailed("Cannot register manager");
        }
    }

    public synchronized Admin registerAdmin(String login, String password) throws AdminValidationFailed {
        final Admin admin = new Admin(login, password, AccessLevel.ADMIN);
        if (validator.validate(admin).isEmpty()) {
                return userRepository.createAdmin(admin.getLogin(), admin.getPassword(), admin.getAccessLevel());
        } else {
            throw new AdminValidationFailed("Cannot register admin");
        }
    }

    public synchronized void updateUser(UUID id, String jws, String firstName, String lastName, String address, String login, String password, AccessLevel accessLevel) throws UserWithGivenIdNotFound, ParseException, JOSEException {
        final User user = userRepository.getUserById(id);
        if (user == null) {
            throw new UserWithGivenIdNotFound("Not found user with given id");
        } else {
            if (this.jwsGenerator.verify(jws)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("uuid", user.getUuid());
                String newJwt = this.jwsGenerator.generateJws(jsonObject.toString());
                if(newJwt.equals(jws)) {
                    userRepository.modifyUser(id, login, password, accessLevel ,firstName, lastName, address);
                }
            }
        }
    }

    public Client getClientById(UUID uuid) throws ClientWithGivenIdNotFound {
            final Client client = (Client) userRepository.getUserById(uuid);
            if (client == null) {
                throw new ClientWithGivenIdNotFound("Not found client with given id");
            }
            return client;
    }

    public User getUserById(UUID id) throws UserWithGivenIdNotFound {
        final User user = userRepository.getUserById(id);
        if (user == null) {
            throw new UserWithGivenIdNotFound("Not found user with given id");
        }
        return user;
    }

    public List<User> findClientsByLoginPart(String login) {
        return findClients(user -> user.getLogin().contains(login));
    }

    public User findUserByLogin(String login, String password) {
        return userRepository.findUserByLogin(login, password);
    }

    public List<User> findClients(Predicate<User> predicate) {
        return userRepository.getUsersBy(predicate);
    }

    public void activateUser(UUID id, String jws) throws UserWithGivenIdNotFound, JOSEException, ParseException {
        if (this.jwsGenerator.verify(jws)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uuid", id);
            String newJwt = this.jwsGenerator.generateJws(jsonObject.toString());
            if(newJwt.equals(jws)) {
                userRepository.activateUser(getUserById(id).getUuid());
            }
        }
    }

    public void deactivateUser(UUID id, String jws) throws UserWithGivenIdNotFound, JOSEException, ParseException {
        if (this.jwsGenerator.verify(jws)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uuid", id);
            String newJwt = this.jwsGenerator.generateJws(jsonObject.toString());
            if(newJwt.equals(jws)) {
                userRepository.deactivateUser(getUserById(id).getUuid());
            }
        }
    }

    public List<User> getAllUsers(){
        return userRepository.getUsers();
    }

    public void deleteUser(UUID id) throws UserWithGivenIdNotFound {
        userRepository.deleteUser(getUserById(id).getUuid());
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        User user = getAllUsers().stream().filter(user1 -> user1.getLogin().equals(securityContext.getUserPrincipal().getName())).findFirst().orElse(null);
        if(user != null && !oldPassword.equals(newPassword)) {
            user.setPassword(newPassword);
            return true;
        }
        if(oldPassword.equals(newPassword)) {
            throw new PasswordMatch("New password and old password matches");
        }
        return false;
    }

}
