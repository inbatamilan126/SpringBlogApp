package com.inbatamilan.BlogAppScaler.articles.dtos;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@Data
@Setter(AccessLevel.NONE)
public class CreateArticleRequest {
    @NonNull
    private String title;
    @NonNull
    private String body;
    @Nullable
    private String subtitle;
}
