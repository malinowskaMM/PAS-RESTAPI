package pl.pas.hotel.dto.user.mapper;

import jakarta.ejb.Stateless;
import pl.pas.hotel.dto.user.AdminDto;
import pl.pas.hotel.model.user.admin.Admin;

@Stateless
public class AdminDtoMapper {

    public Admin toAdmin(AdminDto adminDto) {
        return new Admin(adminDto.getLogin());
    }
}
