package pl.pas.hotel.model.user.client;

import lombok.*;
import pl.pas.hotel.model.user.AccessLevel;
import pl.pas.hotel.model.user.User;

@Getter
public class Client extends User {

    private final String personalId;

    @Setter
    private String firstName;

    @Setter
    private String lastName;

    @Setter
    private String address;

    private Double moneySpent;

    public Client(String personalId, String firstName, String lastName, String address, String login, String password, AccessLevel accessLevel) {
        super(true, login, password, accessLevel);
        this.personalId = personalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.moneySpent = 0.0;
    }

    public Double updateMoneySpent(Double cost) {
        moneySpent += cost;
        return moneySpent;
    }

}
