package pl.pas.hotel.dto.user;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import lombok.Getter;

@Getter
public class ManagerDto extends UserDto{

    @JsonbCreator
    public ManagerDto(@JsonbProperty("login")String login, @JsonbProperty("password")String password, @JsonbProperty("accessLevel")String accessLevel) {
        super(login, password, accessLevel);
    }
}
