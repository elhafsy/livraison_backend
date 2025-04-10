package com.livraison.livraison.service;

import com.livraison.livraison.dto.ClientDTO;
import com.livraison.livraison.dto.UserDTO;
import com.livraison.livraison.dto.request.InfoClientRequest;
import com.livraison.livraison.mapper.ClientMapper;
import com.livraison.livraison.mapper.UserMapper;
import com.livraison.livraison.model.Client;
import com.livraison.livraison.model.User;
import com.livraison.livraison.repository.ClientRepository;
import com.livraison.livraison.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClientRepository clientRepository;

    public UserDTO registerinfoClient(InfoClientRequest request, Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Client client = new Client();
            client.setAddress(request.getAddress());
            client.setPhoneNumber(request.getPhoneNumber());
            client.setCity(request.getCity());
            client.setPostalCode(request.getPostalCode());
            client.setDefaultPaymentMethod(request.getDefaultPaymentMethod());
            client.setUser(user);
            clientRepository.save(client);
            return UserMapper.INSTANCE.userToUserDTO(user);
        }else {
            return null;
        }

    }

    public ClientDTO getInfoClent(Long id) {
        Optional<Client> clientOptional = clientRepository.findByUserId(id);
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            return ClientMapper.INSTANCE.clientToClientDTO(client);
        }
        return null;
    }
}
