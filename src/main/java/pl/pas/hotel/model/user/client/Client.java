package pl.pas.hotel.model.user.client;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.pas.hotel.model.user.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Client extends User {

    @NotNull
    @Size(min = 11, max = 11)
    @Column(name = "PERSONAL_ID", length = 11)
    private String personalId;

    @NotNull
    @Size(max = 35)
    @Column(name = "FIRST_NAME", nullable = false, length = 35)
    @Setter
    private String firstName;

    @NotNull
    @Size(max = 35)
    @Column(name = "LAST_NAME", nullable = false, length = 35)
    @Setter
    private String lastName;

    @NotNull
    @Setter
    private Address address;

    @NotNull
    @PositiveOrZero
    @Column(name = "MONEY_SPENT", nullable = false, columnDefinition = "FLOAT CHECK (MONEY_SPENT >= 0)")
    private Double moneySpent;


    public Client(String personalId, String firstName, String lastName, Address address, String login) {
        super(false, login);
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
    public Client getUser() {
        return this;
    }

}
