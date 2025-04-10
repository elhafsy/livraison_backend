package com.livraison.livraison.service;

import com.livraison.livraison.dto.UserDTO;
import com.livraison.livraison.dto.request.LoginRequest;
import com.livraison.livraison.dto.request.RegisterRequest;
import com.livraison.livraison.mapper.UserMapper;
import com.livraison.livraison.model.enums.Role;
import com.livraison.livraison.model.enums.Status;
import com.livraison.livraison.model.User;
import com.livraison.livraison.repository.UserRepository;
import com.livraison.livraison.security.jwt.JwtUtils;
import com.livraison.livraison.security.services.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private  final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public Map<String,Object> authenticateUser(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getUsername());

        if (userOptional.isEmpty()) {
            throw new BadCredentialsException("Incorrect password or email");
        }

        User user = userOptional.get();

        if (user.getStatus() == Status.PENDING){
            throw new BadCredentialsException("Your account is being processed");
        }

        if (user.getStatus() == Status.REJECTED){
            throw new BadCredentialsException("Your account has been declined");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        user.setOnline(true);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateToken(loginRequest.getUsername());
        String refreshToken = jwtUtils.generateToken(loginRequest.getUsername()); // Remplace ceci par une vraie gestion des tokens

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Récupérer le seul rôle de l'utilisateur
        String role = userDetails.getAuthorities().stream()
                .findFirst() // Prendre le premier (et unique) rôle
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_USER"); // Valeur par défaut si aucun rôle n'est trouvé

        // Création de la réponse avec les infos utilisateur
        Map<String, Object> response = new HashMap<>();
        response.put("access", jwt);
        response.put("refresh", refreshToken);
        response.put("id", userDetails.getId());
        response.put("username", userDetails.getUsername());
        response.put("email", userDetails.getEmail());
        response.put("role", role); // Un seul rôle au lieu d'une liste

        return response;
    }

    public String registerUser(RegisterRequest request){
        if (userRepository.existsByEmail(request.getEmail())) {
            return "Email already in use";
        }

        User user = new User();
        user.setUsername(request.getNom());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(Status.PENDING);
        user.setRole(Role.CLIENT);

        userRepository.save(user);
        return "Your account has been successfully created. It is awaiting validation.";
    }

    public UserDTO getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if( authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl){
            String email = ((UserDetailsImpl) authentication.getPrincipal()).getEmail();
            return userRepository.findByEmail(email)
                    .map(userMapper::userToUserDTO)
                    .orElse(null);
        }
        return null;
    }
}
