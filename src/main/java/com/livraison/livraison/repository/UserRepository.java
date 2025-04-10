package com.livraison.livraison.repository;

import com.livraison.livraison.model.User;
import com.livraison.livraison.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    void deleteByIdIn(List<Long> ids);
    List<User> findAllByOrderByCreatedAtDesc();

    boolean existsByEmail(String email);

    List<User> getUsersByRole(Role role);
}
