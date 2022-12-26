package pl.pas.hotel.dto.user.mapper;

import pl.pas.hotel.dto.user.AdminDto;
import pl.pas.hotel.dto.user.ClientDto;
import pl.pas.hotel.dto.user.ManagerDto;
import pl.pas.hotel.dto.user.UserDto;
import pl.pas.hotel.model.user.User;
import pl.pas.hotel.model.user.admin.Admin;
import pl.pas.hotel.model.user.client.Client;
import pl.pas.hotel.model.user.manager.Manager;


public class UserDtoMapper {

    public User toUser(UserDto userDto) {
        if(userDto instanceof AdminDto) {
            return new Admin(userDto.getLogin());
        } else if(userDto instanceof ManagerDto) {
            return new Manager(userDto.getLogin());
        } else if(userDto instanceof ClientDto) {
            return new Client(((ClientDto) userDto).getPersonalId(), ((ClientDto) userDto).getFirstName(), ((ClientDto) userDto).getLastName(), ((ClientDto) userDto).getAddress(), userDto.getLogin());
        }
        return null;
    }

}
