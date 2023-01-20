package com.store.controller;

import com.store.payload.request.UpgradeRequest;
import com.store.payload.response.MessageResponse;
import com.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/blackFriday/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/upgrade")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> upgradeUser(@Valid @RequestBody UpgradeRequest upgradeRequest) {
        userService.upgradeUser(upgradeRequest);
        return ResponseEntity.ok(new MessageResponse("User upgraded successfully!"));
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (userService.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }
}
