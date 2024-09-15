package com.retailtech.quickcasheasy.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class UserRepositoryImpl implements UserRepository {

    // In-memory storage for users
    private final Map<String, User> userStorage = new HashMap<>();

    @Override
    public void saveUser(User user) {
        userStorage.put(user.getUserName(), user);  // Save user by username
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return Optional.ofNullable(userStorage.get(username));  // Retrieve user by username
    }

    @Override
    public void deleteUserByUsername(String username) {
        userStorage.remove(username);  // Remove user by username
    }
}
