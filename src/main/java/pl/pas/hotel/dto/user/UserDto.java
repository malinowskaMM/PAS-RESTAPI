package pl.pas.hotel.dto.user;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    @NotNull
    @Size(min = 5)
    private String login;

    @NotNull
    @Size(min = 9)
    private String password;

    @NotNull
    private String accessLevel;

    @JsonbCreator
    public UserDto(@JsonbProperty("login")String login, @JsonbProperty("password")String password, @JsonbProperty("accessLevel")String accessLevel) {
        this.login = login;
        this.password = password;
        this.accessLevel = accessLevel;
    }
}
