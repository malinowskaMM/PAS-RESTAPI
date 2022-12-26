package pl.pas.hotel.dto.user;

import lombok.Getter;

@Getter
public class ManagerDto extends UserDto{

    public String getLogin() {
        return super.getLogin();
    }

    public ManagerDto(String login) {
        super(login);
    }
}
