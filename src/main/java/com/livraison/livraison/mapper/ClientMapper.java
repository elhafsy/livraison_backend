package com.livraison.livraison.mapper;

import com.livraison.livraison.dto.ClientDTO;
import com.livraison.livraison.dto.UserDTO;
import com.livraison.livraison.model.Client;
import com.livraison.livraison.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    ClientDTO clientToClientDTO(Client client);

    Client clientDTOToClient(ClientDTO clientDTO);
}
