package com.livraison.livraison.service;

import com.livraison.livraison.dto.UserDTO;
import com.livraison.livraison.mapper.UserMapper;
import com.livraison.livraison.model.enums.Status;
import com.livraison.livraison.model.User;
import com.livraison.livraison.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final WebSocketNotificationService webSocketNotificationService;

    public String updateUserStatus(Long userId, String status) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()){
            return "User not found";
        }
        User user = userOptional.get();
        if (status.equalsIgnoreCase("ACTIVE")){
            user.setStatus(Status.ACTIVE);
        } else if (status.equalsIgnoreCase("REJECTED")) {
            user.setStatus(Status.REJECTED);
        }else if (status.equalsIgnoreCase("PENDING")) {
            user.setStatus(Status.PENDING);
        }else {
            return "Invalid status";
        }

        userRepository.save(user);

        return "Status updated successfully";

    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAllByOrderByCreatedAtDesc();
        return users.stream().map(userMapper::userToUserDTO).collect(Collectors.toList());
    }

    @Transactional
    public void deleteUserByIds(List<Long> ids) {
        userRepository.deleteByIdIn(ids);
    }


}
