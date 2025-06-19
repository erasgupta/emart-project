package com.portal.emart.api.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.portal.emart.api.component.JwtUtil;
import com.portal.emart.api.component.SmsSender;
import com.portal.emart.api.dao.UserRepository;
import com.portal.emart.api.entity.Customers;
import com.portal.emart.api.model.SendCodeResponse;
import com.portal.emart.api.model.SignUpRequest;
import com.portal.emart.api.model.User;

@Service
public class UserService {
    
    private final JavaMailSender mailSender;
    private final SmsSender smsSender;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    // constructor
    public UserService(JavaMailSender mailSender, SmsSender smsSender, JwtUtil jwtUtil,UserRepository userRepository) {
        this.mailSender = mailSender;
        this.smsSender = smsSender;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }
    
    
    private final Map<String, String> otpStore = new HashMap<>();
  //  private final List<User> users = Arrays.asList(
    //        new User("Ashish","sunny2021@gmail.com", "1234567890"),
      //      new User("Jane","jane@example.com", "9876543210")
    //);

    public SendCodeResponse sendCode(String username) {
        boolean isEmail = username.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$");
        boolean isPhone = username.matches("^\\d{10}$");

        if (!isEmail && !isPhone) {
            return new SendCodeResponse(false, null, "Invalid email or phone format");
        }

        User user = getUserDtoByEmailOrPhone(username, username);
        
        //Optional<User> user = users.stream()
          //      .filter(u -> u.getEmail().equalsIgnoreCase(username) || u.getPhoneNumber().equals(username))
            //    .findFirst();

        if (user == null) {
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
        String token = jwtUtil.generateToken(username);

        return new SendCodeResponse(true, token, "Verification successful");
    }
    
    public User getUserByUsername(String username) {
        return getUserDtoByEmailOrPhone(username, username);
    	//User user = new User("Ashish","sunny2021@gmail.com","9654018490");
    	//return user;
    }
    
    public String registerUser(SignUpRequest request) {
        if (userRepository.existsByEmail(request.email)) {
            return "Email already registered";
        }
        if (userRepository.existsByPhone(request.phone)) {
            return "Phone number already registered";
        }

        Customers customers = new Customers();
        customers.setFirstName(request.firstName);
        customers.setLastName(request.lastName);
        customers.setEmail(request.email);
        customers.setPhone(request.phone);

        userRepository.save(customers);
        return "User registered successfully";
    }
    
    public User getUserDtoByEmailOrPhone(String email, String phone) {
        Customers customer = userRepository.findByEmailOrPhone(email, phone);
        User user = null;
        if (customer != null) {
            user = new User(
                    customer.getFirstName(),
                    customer.getEmail(),
                    customer.getPhone()
                );
        }

        return user;
    }

}
