package com.livraison.livraison.mapper;

import com.livraison.livraison.dto.UserDTO;
import com.livraison.livraison.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

   @Mapping(target = "password", ignore = true)
    @Mapping(source = "username", target = "name")
    UserDTO userToUserDTO(User user);

    User userDTOToUser(UserDTO userDTO);
}
