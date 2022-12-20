package pl.pas.hotel.dto.user.mapper;

import jakarta.ejb.Stateless;
import pl.pas.hotel.dto.user.ManagerDto;
import pl.pas.hotel.model.user.manager.Manager;

@Stateless
public class ManagerDtoMapper {

    public Manager toManager(ManagerDto managerDto) {
        return new Manager(managerDto.getLogin());
    }
}
