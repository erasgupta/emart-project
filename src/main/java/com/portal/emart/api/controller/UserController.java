package com.portal.emart.api.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portal.emart.api.model.SendCodeRequest;
import com.portal.emart.api.model.SendCodeResponse;
import com.portal.emart.api.model.SignUpRequest;
import com.portal.emart.api.model.User;
import com.portal.emart.api.model.VerifyCodeRequest;
import com.portal.emart.api.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

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
    
    @GetMapping("/getUserDetails")
    public ResponseEntity<?> getUserDetails(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        User user = userService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        return ResponseEntity.ok(user);
    }
    
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        String result = userService.registerUser(request);

        if (result.equals("User registered successfully")) {
            return ResponseEntity.ok(Map.of("success", true, "message", result));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(Map.of("success", false, "message", result));
        }
    }

}
