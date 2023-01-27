package pl.pas.hotel.dto.user.mapper;

import pl.pas.hotel.dto.user.*;
import pl.pas.hotel.model.user.AccessLevel;
import pl.pas.hotel.model.user.User;
import pl.pas.hotel.model.user.admin.Admin;
import pl.pas.hotel.model.user.client.Client;
import pl.pas.hotel.model.user.manager.Manager;


public class UserDtoMapper {

    public User toUser(UserDto userDto) {
        if(userDto instanceof AdminDto) {
            return new Admin(userDto.getLogin(), userDto.getPassword(), AccessLevel.valueOf(userDto.getAccessLevel()));
        } else if(userDto instanceof ManagerDto) {
            return new Manager(userDto.getLogin(), userDto.getPassword(), AccessLevel.valueOf(userDto.getAccessLevel()));
        } else if(userDto instanceof ClientDto) {
            return new Client(((ClientDto) userDto).getPersonalId(), ((ClientDto) userDto).getFirstName(), ((ClientDto) userDto).getLastName(), ((ClientDto) userDto).getAddress(), userDto.getLogin(), userDto.getPassword(), AccessLevel.valueOf(userDto.getAccessLevel()));
        }
        return null;
    }

    public static ShowUserDto toShowUserDto(User user) {
        return new ShowUserDto(user.getUuid() ,user.getLogin(), user.getAccessLevel().getAccessLevel());
    }

}
