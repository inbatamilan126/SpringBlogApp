package com.inbatamilan.BlogAppScaler.users;

import com.inbatamilan.BlogAppScaler.users.dtos.CreateUserRequest;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private final UserRepository userRepository;

    public UsersService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity createUser(CreateUserRequest request) {
        var newUser = UserEntity.builder()
                .username(request.getUsername())
                //.password(request.getPassword()) // TODO: encrypt password
                .email(request.getEmail())
                .build();
        return userRepository.save(newUser);
    }

    public UserEntity getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(userId));
    }

    public UserEntity getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(username));
    }

    public UserEntity loginUser(String username, String password) {
        var user = userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(username));
        // TODO: check the password
        return user;
    }

    public static class UserNotFoundException extends IllegalArgumentException {

        public UserNotFoundException(String username) {
            super("User with username: " + username + " not found");
        }

        public UserNotFoundException(Long userId) {
            super("User with id: " + userId + " not found");
        }
    }
}
