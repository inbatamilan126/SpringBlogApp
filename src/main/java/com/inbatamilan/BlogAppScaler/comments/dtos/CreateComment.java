package com.inbatamilan.BlogAppScaler.comments.dtos;

import lombok.Data;
import org.jspecify.annotations.NonNull;

@Data
public class CreateComment {

    private String title;
    @NonNull
    private String body;
}
