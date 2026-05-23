package com.inbatamilan.BlogAppScaler.articles.dtos;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.jspecify.annotations.Nullable;

@Data
@Setter(AccessLevel.NONE)
@Builder
public class ArticleResponse {
    private String title;
    private String slug;
    private String body;
    private String subtitle;
    private String author;
}
