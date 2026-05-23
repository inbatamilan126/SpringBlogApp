package com.inbatamilan.BlogAppScaler.comments;

import com.inbatamilan.BlogAppScaler.articles.ArticleEntity;
import com.inbatamilan.BlogAppScaler.articles.ArticlesRepository;
import com.inbatamilan.BlogAppScaler.comments.dtos.CommentResponse;
import com.inbatamilan.BlogAppScaler.comments.dtos.CreateComment;
import com.inbatamilan.BlogAppScaler.users.UserEntity;
import org.modelmapper.ModelMapper;
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

    public List<CommentResponse> getCommentsByArticleSlug(String articleSlug) {
        var comments = commentsRepository.getCommentEntitiesByArticle_Slug(articleSlug);
        List<CommentResponse> commentResponses = new ArrayList<>();
        for (CommentEntity comment : comments) {
            commentResponses.add(
                    new CommentResponse(
                            comment.getId(),
                            comment.getTitle(),
                            comment.getBody(),
                            comment.getCreatedAt(),
                            comment.getAuthor().getUsername())
            );
        }
        return commentResponses;
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
