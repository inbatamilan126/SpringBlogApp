package com.inbatamilan.BlogAppScaler.users;

import com.inbatamilan.BlogAppScaler.users.dtos.UserRequest;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UserRepository userRepository, ModelMapper modelMapper,
                        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity createUser(UserRequest request) {
        UserEntity newUser = modelMapper.map(request, UserEntity.class);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
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
        // TODO: throw invalid credential exception when username is wrong
        var user = userRepository.findByUsername(username).orElseThrow(InvalidCredentialsException::new);
        var passMatch = passwordEncoder.matches(password, user.getPassword());
        if (!passMatch) throw new InvalidCredentialsException();
        return user;
    }

    public UserEntity updateUser(UserRequest request, Long id) {
        UserEntity savedUser = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(id)
        );
        modelMapper.map(request, savedUser);
        return userRepository.save(savedUser);
    }

    public void deleteUser(Long id) {
        UserEntity entity = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(id)
        );
        userRepository.delete(entity);
    }

    public static class UserNotFoundException extends IllegalArgumentException {

        public UserNotFoundException(String username) {
            super("User with username: " + username + " not found");
        }

        public UserNotFoundException(Long userId) {
            super("User with id: " + userId + " not found");
        }
    }

    public static class InvalidCredentialsException
            extends IllegalArgumentException {
        public InvalidCredentialsException() {
            super("Invalid username or password combination");
        }
    }
}
