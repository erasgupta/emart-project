package com.portal.emart.api.service;

import com.portal.emart.api.component.SmsSender;
import com.portal.emart.api.model.SendCodeResponse;
import com.portal.emart.api.model.User;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private Map<Long, User> userRepo = new HashMap<>();
    private Long currentId = 1L;
    
    private final JavaMailSender mailSender;
    private final SmsSender smsSender;
    // constructor
    public UserService(JavaMailSender mailSender, SmsSender smsSender) {
        this.mailSender = mailSender;
        this.smsSender = smsSender;
    }
    
    
    private final Map<String, String> otpStore = new HashMap<>();
    private final List<User> users = Arrays.asList(
            new User("sunny2021@gmail.com", "1234567890"),
            new User("jane@example.com", "9876543210")
    );

    public SendCodeResponse sendCode(String username) {
        boolean isEmail = username.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$");
        boolean isPhone = username.matches("^\\d{10}$");

        if (!isEmail && !isPhone) {
            return new SendCodeResponse(false, null, "Invalid email or phone format");
        }

        Optional<User> user = users.stream()
                .filter(u -> u.getEmail().equals(username) || u.getPhoneNumber().equals(username))
                .findFirst();

        if (user.isEmpty()) {
            return new SendCodeResponse(false, "USER_NOT_FOUND", "User not found");
        }

        String code = String.valueOf(new Random().nextInt(900000) + 100000);
        otpStore.put(username, code);

        // Simulate sending code
        if (isEmail) {
            System.out.printf("Sent code %s to email %s%n", code, username);
            sendEmail(username, code);
        } else {
            System.out.printf("Sent SMS code %s to phone %s%n", code, username);
            smsSender.sendSms(username, code);  // Use +countryCode
        }

        return new SendCodeResponse(true, null, "Verification code sent");
    }
    
    public void sendEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your Verification Code");
        message.setText("Your code is: " + code);
        mailSender.send(message);
    }
    
 // This should be inside your existing VerificationService class
    public SendCodeResponse verifyCode(String username, String code) {
        if (username == null || code == null) {
            return new SendCodeResponse(false, "INVALID_REQUEST", "Username and code are required");
        }

        String storedCode = otpStore.get(username);

        if (storedCode == null) {
            return new SendCodeResponse(false, "NO_CODE_SENT", "No verification code was sent to this user");
        }

        if (!storedCode.equals(code)) {
            return new SendCodeResponse(false, "INVALID_CODE", "Verification code is incorrect");
        }

        // Optionally clear the code after successful verification
        otpStore.remove(username);

        return new SendCodeResponse(true, null, "Verification successful");
    }


    public User createUser(User user) {
        user.setId(currentId++);
        userRepo.put(user.getId(), user);
        return user;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userRepo.values());
    }

    public User getUserById(Long id) {
        return userRepo.get(id);
    }

    public User updateUser(Long id, User userDetails) {
        User existingUser = userRepo.get(id);
        if (existingUser != null) {
            existingUser.setName(userDetails.getName());
            existingUser.setEmail(userDetails.getEmail());
        }
        return existingUser;
    }

    public void deleteUser(Long id) {
        userRepo.remove(id);
    }
}
