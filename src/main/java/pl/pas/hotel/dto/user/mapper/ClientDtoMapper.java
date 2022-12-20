package pl.pas.hotel.dto.user.mapper;

import jakarta.ejb.Stateless;
import pl.pas.hotel.dto.user.ClientDto;
import pl.pas.hotel.model.user.client.Client;

@Stateless
public class ClientDtoMapper {

    public Client toClient(ClientDto clientDto) {
        return new Client(clientDto.getPersonalId(), clientDto.getFirstName(), clientDto.getLastName(), clientDto.getAddress(), clientDto.getLogin());

    }
}
