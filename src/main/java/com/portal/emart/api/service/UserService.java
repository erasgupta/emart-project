package com.portal.emart.api.service;

import com.portal.emart.api.model.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private Map<Long, User> userRepo = new HashMap<>();
    private Long currentId = 1L;

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
