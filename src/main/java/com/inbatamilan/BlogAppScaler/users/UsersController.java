package com.inbatamilan.BlogAppScaler.users;

import com.inbatamilan.BlogAppScaler.common.dtos.ErrorResponse;
import com.inbatamilan.BlogAppScaler.security.JWTService;
import com.inbatamilan.BlogAppScaler.users.dtos.UserRequest;
import com.inbatamilan.BlogAppScaler.users.dtos.LoginUserRequest;
import com.inbatamilan.BlogAppScaler.users.dtos.UserResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;
    private final ModelMapper modelMapper;
    private final JWTService jwtService;

    public UsersController(UsersService usersService, ModelMapper modelMapper,
                           JWTService jwtService) {
        this.usersService = usersService;
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;
    }

    @PostMapping("")
    ResponseEntity<UserResponse> signupUser(@RequestBody
                                            UserRequest request) {
        UserEntity savedUser = usersService.createUser(request);
        URI savedUserUri = URI.create("/users/" + savedUser.getId());
        var savedUserResponse = modelMapper.map(savedUser, UserResponse.class);
        savedUserResponse.setToken(
                jwtService.createJwt(savedUser.getId())
        );
        return ResponseEntity
                .created(savedUserUri)
                .body(savedUserResponse);
    }

    @PreAuthorize("#id == authentication.principal.id")
    @PutMapping("/{id}")
    ResponseEntity<UserResponse> updateUserById(@RequestBody UserRequest request,
                                            @PathVariable Long id) {
        var savedUser = usersService.updateUser(request, id);
        return ResponseEntity.ok(
                modelMapper.map(savedUser, UserResponse.class)
        );
    }

    @PreAuthorize("#id == authentication.principal.id")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        usersService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal UserEntity user) {
        UserResponse response = modelMapper.map(user, UserResponse.class);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    ResponseEntity<UserResponse> updateUserMe(@RequestBody UserRequest request,
                                              @AuthenticationPrincipal UserEntity user) {
        var savedUser = usersService.updateUser(request, user.getId());
        return ResponseEntity.ok(
                modelMapper.map(savedUser, UserResponse.class)
        );
    }

    @DeleteMapping("/me")
    ResponseEntity<Void> deleteUserMe(@AuthenticationPrincipal UserEntity user) {
        usersService.deleteUser(user.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    ResponseEntity<UserResponse> loginUser(@RequestBody
                                           LoginUserRequest request) {
        UserEntity savedUser = usersService.loginUser(
                request.getUsername(), request.getPassword()
        );
        var userResponse = modelMapper.map(savedUser, UserResponse.class);
        userResponse.setToken(
                jwtService.createJwt(savedUser.getId())
        );
        return ResponseEntity.ok(userResponse);
    }

    @ExceptionHandler({
            UsersService.UserNotFoundException.class,
            UsersService.InvalidCredentialsException.class
    })
    ResponseEntity<ErrorResponse> handleUserExceptions(Exception exp) {
        String message;
        HttpStatus status;

        if (exp instanceof UsersService.UserNotFoundException) {
            message = exp.getMessage();
            status = HttpStatus.NOT_FOUND;
        } else if (exp instanceof UsersService.InvalidCredentialsException) {
            message = exp.getMessage();
            status = HttpStatus.UNAUTHORIZED;
        } else {
            message = "Something went wrong";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        ErrorResponse response = ErrorResponse.builder()
                .message(message)
                .build();

        return ResponseEntity.status(status).body(response);
    }
}
