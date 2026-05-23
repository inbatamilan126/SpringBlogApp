package com.inbatamilan.BlogAppScaler.comments;

import com.inbatamilan.BlogAppScaler.comments.dtos.CommentResponse;
import com.inbatamilan.BlogAppScaler.comments.dtos.CreateComment;
import com.inbatamilan.BlogAppScaler.users.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles/{article-slug}/comments")
public class CommentsController {

    private final CommentsService commentsService;

    public CommentsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable("article-slug") String articleSlug) {
        return ResponseEntity.ok(commentsService.getCommentsByArticleSlug(articleSlug));
    }

    @PostMapping()
    public ResponseEntity<CommentResponse> createComment(@RequestBody CreateComment comment,
                                                @PathVariable("article-slug") String articleSlug,
                                                @AuthenticationPrincipal UserEntity commenter) {
        CommentResponse response = commentsService.saveComment(comment, articleSlug, commenter);
        return ResponseEntity.ok(response);
    }
}
