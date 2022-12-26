package pl.pas.hotel.dto.user;

import lombok.Getter;

@Getter
public class AdminDto extends UserDto {

    public AdminDto(String login, String password, String accessLevel) {
        super(login, password, accessLevel);
    }
}
