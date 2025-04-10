package com.livraison.livraison.controller;

import com.livraison.livraison.dto.ClientDTO;
import com.livraison.livraison.dto.UserDTO;
import com.livraison.livraison.dto.request.InfoClientRequest;
import com.livraison.livraison.security.services.UserDetailsImpl;
import com.livraison.livraison.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @PostMapping
    public UserDTO registerinfoClient(@Valid @RequestBody InfoClientRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails ) {
        return clientService.registerinfoClient(request,userDetails.getId());
    }
    @GetMapping("/info")
    public ResponseEntity<ClientDTO> getInfoClient(@AuthenticationPrincipal UserDetailsImpl userDetails ) {
        ClientDTO clientDTO = clientService.getInfoClent(userDetails.getId());
        if (clientDTO != null) {
            return ResponseEntity.ok(clientDTO);
        }
        return ResponseEntity.notFound().build();
    }
}
