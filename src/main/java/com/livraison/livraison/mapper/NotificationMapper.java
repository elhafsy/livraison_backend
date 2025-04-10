package com.livraison.livraison.mapper;


import com.livraison.livraison.dto.NotificationDTO;
import com.livraison.livraison.model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    //@Mapping(source = "createdAt", target = "createdAt")
    NotificationDTO notificationToNotificationDTO(Notification notification);

    Notification notificationDTOToNotification(NotificationDTO notificationDTO);
}
