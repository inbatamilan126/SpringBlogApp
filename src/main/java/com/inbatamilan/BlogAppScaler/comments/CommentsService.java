package com.inbatamilan.BlogAppScaler.comments;

import com.inbatamilan.BlogAppScaler.articles.ArticleEntity;
import com.inbatamilan.BlogAppScaler.articles.ArticlesRepository;
import com.inbatamilan.BlogAppScaler.comments.dtos.CommentResponse;
import com.inbatamilan.BlogAppScaler.comments.dtos.CreateComment;
import com.inbatamilan.BlogAppScaler.common.dtos.PagedResponse;
import com.inbatamilan.BlogAppScaler.users.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsService {

    private final CommentsRepository commentsRepository;
    private final ArticlesRepository articlesRepository;
    private final ModelMapper modelMapper;


    public CommentsService(CommentsRepository commentsRepository, ArticlesRepository articlesRepository, ModelMapper modelMapper) {
        this.commentsRepository = commentsRepository;
        this.articlesRepository = articlesRepository;
        this.modelMapper = modelMapper;
    }

    public PagedResponse<CommentResponse> getCommentsByArticleSlug(String articleSlug, Pageable pageable) {
        Page<CommentEntity> commentsPage = commentsRepository.getCommentEntitiesByArticle_Slug(articleSlug, pageable);
        Page<CommentResponse> responsePage = commentsPage.map(comment ->
                CommentResponse.builder()
                        .id(comment.getId())
                        .body(comment.getBody())
                        .title(comment.getTitle())
                        .author(comment.getAuthor().getUsername())
                        .build()
        );
        return new PagedResponse<CommentResponse>(responsePage);
    }

    public CommentResponse saveComment(CreateComment comment, String articleSlug, UserEntity commenter) {
        ArticleEntity article = articlesRepository.findBySlug(articleSlug);
        CommentEntity toSave = modelMapper.map(comment, CommentEntity.class);
        toSave.setAuthor(commenter);
        toSave.setArticle(article);
        var saved = commentsRepository.save(toSave);
        return new CommentResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getBody(),
                saved.getCreatedAt(),
                saved.getAuthor().getUsername()
        );
    }
}
