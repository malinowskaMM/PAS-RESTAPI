package pl.pas.hotel.dto.user;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ClientDto extends UserDto{

    @NotNull
    @Size(min = 11, max = 11)
    @Column(name = "personalId", length = 11)
    private String personalId;

    @NotNull
    @Size(max = 35)
    @Column(name = "firstName", nullable = false, length = 35)
    private String firstName;

    @NotNull
    @Size(max = 35)
    @Column(name = "lastName", nullable = false, length = 35)
    private String lastName;

    @NotNull
    private String address;

    @NotNull
    @PositiveOrZero
    @Column(name = "moneySpent", nullable = false, columnDefinition = "FLOAT CHECK (MONEY_SPENT >= 0)")
    private Double moneySpent;

    public ClientDto(boolean isActive, String login, String personalId, String firstName, String lastName, String address, Double moneySpent) {
        super(isActive, login);
        this.personalId = personalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.moneySpent = moneySpent;
    }
}
