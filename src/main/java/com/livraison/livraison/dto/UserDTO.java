package com.livraison.livraison.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.livraison.livraison.model.enums.Role;
import com.livraison.livraison.model.enums.Status;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
public class UserDTO {
    private Long id;

    //@JsonProperty("name")
    private String name;

    private String email;
    private String password;
    private Role role ;
    private Status status ;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
