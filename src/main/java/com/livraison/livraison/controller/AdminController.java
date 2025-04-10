package com.livraison.livraison.controller;

import com.livraison.livraison.dto.UserDTO;
import com.livraison.livraison.service.AdminService;
import com.livraison.livraison.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final NotificationService notificationService;

    @PutMapping("/validate/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> validateUser(@PathVariable Long userId, @RequestParam String status) {
        String response = adminService.updateUserStatus(userId,status);
        HashMap<String,String> msg = new HashMap<>();
        msg.put("message", response);
        return response.equals("Status updated successfully") ?
                ResponseEntity.ok(msg) : ResponseEntity.badRequest().body(response);

    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @DeleteMapping("/deleteUsers")
    public ResponseEntity<?> deleteUsers(@RequestBody List<Long> ids) {
        adminService.deleteUserByIds(ids);
        HashMap<String,String> msg = new HashMap<>();
        msg.put("message", "User deleted successfully");
        return ResponseEntity.ok(msg);
    }


}
