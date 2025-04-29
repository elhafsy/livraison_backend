package com.livraison.livraison.service;

import com.livraison.livraison.dto.UserDTO;
import com.livraison.livraison.dto.request.InfoClientRequest;
import com.livraison.livraison.mapper.UserMapper;
import com.livraison.livraison.model.Client;
import com.livraison.livraison.model.User;
import com.livraison.livraison.repository.ClientRepository;
import com.livraison.livraison.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public UserDTO getUser(Long idUser){
        Optional<User> user = userRepository.findById(idUser);
        return user.map(UserMapper.INSTANCE::userToUserDTO).orElse(null);
    }
}
