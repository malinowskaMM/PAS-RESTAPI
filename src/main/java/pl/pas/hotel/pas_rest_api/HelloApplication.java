package pl.pas.hotel.pas_rest_api;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import pl.pas.hotel.model.user.AccessLevel;
import pl.pas.hotel.repositories.RoomRepository;
import pl.pas.hotel.repositories.UserRepository;
import pl.pas.hotel.repositoriesImplementation.RentRepository;

import java.time.LocalDateTime;

@ApplicationPath("/api")
@ApplicationScoped
//@DeclareRoles({"ADMIN", "MANAGER", "CLIENT", "NONE"})
//@RolesAllowed({"ADMIN", "MANAGER", "CLIENT", "NONE"})
public class HelloApplication extends Application {

    @Inject
    UserRepository userRepository;

    @Inject
    RoomRepository roomRepository;

    @Inject
    RentRepository rentRepository;

    @PostConstruct
    public void addAdmin() {
        roomRepository.createRoom(1, 100.0, 2);


            userRepository.createAdmin("adminLogin", "adminPassword", AccessLevel.ADMIN);
            userRepository.createManager("managerLogin", "managerPassword", AccessLevel.MANAGER);
            userRepository.createClient("11111111111", "clientName", "clientLastName", "clientAddress", "clientLogin", "clientPassword", AccessLevel.CLIENT);
    }




}