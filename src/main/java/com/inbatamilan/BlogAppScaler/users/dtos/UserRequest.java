package com.inbatamilan.BlogAppScaler.users.dtos;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@Data
@Setter(AccessLevel.NONE)
public class UserRequest {
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String email;
    @Nullable
    private String bio;
    @Nullable
    private String image;
}
