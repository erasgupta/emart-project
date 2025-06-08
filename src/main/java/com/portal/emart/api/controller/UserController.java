package com.portal.emart.api.controller;

import com.portal.emart.api.model.SendCodeRequest;
import com.portal.emart.api.model.SendCodeResponse;
import com.portal.emart.api.model.User;
import com.portal.emart.api.model.VerifyCodeRequest;
import com.portal.emart.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("emart/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @PostMapping("/send-code")
    public ResponseEntity<SendCodeResponse> sendCode(@Validated @RequestBody SendCodeRequest request) {
        return ResponseEntity.ok(userService.sendCode(request.getUsername()));
    }
    
    @PostMapping("/verify-code")
    public ResponseEntity<SendCodeResponse> verifyCode(@Validated @RequestBody VerifyCodeRequest request) {
        return ResponseEntity.ok(
        		userService.verifyCode(request.getUsername(), request.getCode())
        );
    }


    // CREATE
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        return ResponseEntity.ok(savedUser);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
