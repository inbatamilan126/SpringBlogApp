package com.inbatamilan.BlogAppScaler.comments.dtos;

import com.inbatamilan.BlogAppScaler.articles.ArticleEntity;
import com.inbatamilan.BlogAppScaler.users.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    private Long id;

    private String title;

    private String body;

    private Date createdAt;

    private String author;
}
