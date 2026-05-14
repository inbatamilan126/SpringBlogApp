package com.inbatamilan.BlogAppScaler.users.dtos;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jspecify.annotations.NonNull;

@Data
@Setter(AccessLevel.NONE)
public class CreateUserRequest {
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String email;
}
