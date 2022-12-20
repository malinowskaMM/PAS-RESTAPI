package pl.pas.hotel.dto.user;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ClientDto extends UserDto{

    @NotNull
    @Size(min = 11, max = 11)
    private String personalId;

    @NotNull
    @Size(max = 35)
    private String firstName;

    @NotNull
    @Size(max = 35)
    private String lastName;

    @NotNull
    private String address;

    @NotNull
    @PositiveOrZero
    private Double moneySpent;

    @JsonbCreator
    public ClientDto(@JsonbProperty("isActive")boolean isActive, @JsonbProperty("login")String login, @JsonbProperty("personalId")String personalId, @JsonbProperty("firstName")String firstName, @JsonbProperty("isActive")String lastName, @JsonbProperty("address")String address, @JsonbProperty("moneySpent")Double moneySpent) {
        super(isActive, login);
        this.personalId = personalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.moneySpent = moneySpent;
    }
}
