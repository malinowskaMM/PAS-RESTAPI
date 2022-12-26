package pl.pas.hotel.dto.user;

import lombok.Getter;

@Getter
public class AdminDto extends UserDto {


    public String getLogin() {
        return super.getLogin();
    }

    public AdminDto(String login) {
        super(login);
    }
}
