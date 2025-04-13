package com.livraison.livraison.mapper;

import com.livraison.livraison.dto.MessageDTO;
import com.livraison.livraison.dto.NotificationDTO;
import com.livraison.livraison.model.Message;
import com.livraison.livraison.model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    //@Mapping(source = "createdAt", target = "createdAt")
    MessageDTO messageToMessageDTO(Message message);

    Message messageDTOToMessage(MessageDTO messageDTO);
}