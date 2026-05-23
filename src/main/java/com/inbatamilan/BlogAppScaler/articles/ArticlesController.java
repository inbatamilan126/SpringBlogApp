package com.inbatamilan.BlogAppScaler.articles;

import com.inbatamilan.BlogAppScaler.articles.dtos.ArticleResponse;
import com.inbatamilan.BlogAppScaler.articles.dtos.CreateArticleRequest;
import com.inbatamilan.BlogAppScaler.articles.dtos.UpdateArticleRequest;
import com.inbatamilan.BlogAppScaler.common.dtos.ErrorResponse;
import com.inbatamilan.BlogAppScaler.common.dtos.PagedResponse;
import com.inbatamilan.BlogAppScaler.users.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticlesController {

    private final ArticlesService articlesService;

    public ArticlesController(ArticlesService articlesService) {
        this.articlesService = articlesService;
    }

    @GetMapping("")
    public ResponseEntity<PagedResponse<ArticleResponse>> getArticles(Pageable pageable) {
        return ResponseEntity.ok(articlesService.getAllArticles(pageable));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ArticleResponse> getArticleBySlug(
            @PathVariable("slug") String slug) {
        return ResponseEntity.ok(articlesService.getArticleBySlug(slug));
    }

    @PostMapping()
    ResponseEntity<ArticleResponse> createArticle(@AuthenticationPrincipal UserEntity user,
                                                  @RequestBody CreateArticleRequest articleRequest) {
        return ResponseEntity.ok(articlesService.createArticle(articleRequest, user.getId()));
    }

    @PatchMapping("/{slug}")
    public ResponseEntity<ArticleResponse> updateArticle(@PathVariable String slug,
                                                         @RequestBody UpdateArticleRequest updateArticleRequest) {
        return ResponseEntity.ok(articlesService.updateArticleBySlug(slug, updateArticleRequest));
    }

    /* @PatchMapping("/{articleId}")
    public ResponseEntity<ArticleEntity> updateArticle(@PathVariable Long articleId,
                                                       @RequestBody UpdateArticleRequest updateArticleRequest) {
        return ResponseEntity.ok(articlesService.updateArticleById(articleId, updateArticleRequest));
    } */

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
