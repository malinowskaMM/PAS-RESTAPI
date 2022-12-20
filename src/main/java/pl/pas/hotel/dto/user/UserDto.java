package pl.pas.hotel.dto.user;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserDto {


    @NotNull
    private boolean isActive;

    @NotNull
    @Size(min = 5)
    private final String login;

    @JsonbCreator
    public UserDto(@JsonbProperty("isActive")boolean isActive, @JsonbProperty("login")String login) {
        this.isActive = isActive;
        this.login = login;
    }
}
