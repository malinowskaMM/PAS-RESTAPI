package pl.pas.hotel.dto.user.mapper;

import jakarta.ejb.Stateless;
import pl.pas.hotel.dto.user.ClientDto;
import pl.pas.hotel.model.user.client.Client;

@Stateless
public class ClientDtoMapper {

    public Client toClient(ClientDto clientDto) {
        return Client.builder()
                .personalId(clientDto.getPersonalId())
                .firstName(clientDto.getFirstName())
                .lastName(clientDto.getLastName())
                .address(clientDto.getAddress())
                .moneySpent(clientDto.getMoneySpent())
                .build();

    }
}
