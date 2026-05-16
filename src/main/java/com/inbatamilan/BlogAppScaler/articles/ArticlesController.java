package com.inbatamilan.BlogAppScaler.articles;

import com.inbatamilan.BlogAppScaler.common.dtos.ErrorResponse;
import com.inbatamilan.BlogAppScaler.users.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/articles")
public class ArticlesController {

    private final ArticlesService articlesService;

    public ArticlesController(ArticlesService articlesService) {
        this.articlesService = articlesService;
    }

    @GetMapping("")
    public ResponseEntity<List<ArticleEntity>> getArticles() {
        return ResponseEntity.ok(
                StreamSupport.stream(
                        articlesService.getAllArticles().spliterator(), false)
                        .toList()
        );
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ArticleEntity> getArticleBySlug(
            @PathVariable("slug") String slug) {
        return ResponseEntity.ok(articlesService.getArticleBySlug(slug));
    }

    @PostMapping()
    String createArticle(@AuthenticationPrincipal UserEntity user) {
        return "create article called by " + user.getUsername();
    }

    @ExceptionHandler({
            ArticlesService.ArticleNotFoundException.class
    })
    ResponseEntity<ErrorResponse> handleUserExceptions(Exception exp) {
        String message;
        HttpStatus status;

        if (exp instanceof ArticlesService.ArticleNotFoundException) {
            message = exp.getMessage();
            status = HttpStatus.NOT_FOUND;
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
