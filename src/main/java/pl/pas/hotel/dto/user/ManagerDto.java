package pl.pas.hotel.dto.user;

import lombok.Getter;

@Getter
public class ManagerDto extends UserDto{

    public ManagerDto(String login, String password, String accessLevel) {
        super(login, password, accessLevel);
    }
}
