package pl.pas.hotel.dto.auth;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDto {
    @NotNull
    @Size(min = 5)
    String login;

    @NotNull
    @Size(min = 9)
    String password;

    @JsonbCreator
    public AuthDto(@JsonbProperty("login")String login, @JsonbProperty("password")String password) {
        this.login = login;
        this.password = password;
    }

    public AuthDto() {
    }
}
