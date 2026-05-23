package com.inbatamilan.BlogAppScaler.articles;

import com.inbatamilan.BlogAppScaler.articles.dtos.ArticleResponse;
import com.inbatamilan.BlogAppScaler.articles.dtos.CreateArticleRequest;
import com.inbatamilan.BlogAppScaler.articles.dtos.UpdateArticleRequest;
import com.inbatamilan.BlogAppScaler.common.dtos.PagedResponse;
import com.inbatamilan.BlogAppScaler.users.UserRepository;
import com.inbatamilan.BlogAppScaler.users.UsersService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticlesService {

    private final ArticlesRepository articlesRepository;
    private final UserRepository userRepository;

    public ArticlesService(ArticlesRepository articlesRepository,
            UserRepository userRepository) {
        this.articlesRepository = articlesRepository;
        this.userRepository = userRepository;
    }

    public PagedResponse<ArticleResponse> getAllArticles(Pageable pageable) {
        Page<ArticleEntity> articleEntityPage = articlesRepository.findAll(pageable);
        Page<ArticleResponse> articleResponsePage = articleEntityPage.map(article ->
                ArticleResponse.builder()
                        .id(article.getId())
                        .body(article.getBody())
                        .title(article.getTitle())
                        .subtitle(article.getSubtitle())
                        .slug(article.getSlug())
                        .author(article.getAuthor().getUsername())
                        .build()
        );
        return new PagedResponse<ArticleResponse>(articleResponsePage);
    }

    public ArticleResponse getArticleBySlug(String slug) {
        var article = articlesRepository.findBySlug(slug);
        if (article == null) {
            throw new ArticleNotFoundException(slug);
        }
        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .slug(article.getSlug())
                .body(article.getBody())
                .subtitle(article.getSubtitle())
                .author(article.getAuthor().getUsername())
                .build();
    }

    public ArticleResponse createArticle(CreateArticleRequest request,
            Long authorId) {
        var author = userRepository.findById(authorId)
                .orElseThrow(() ->
                        new UsersService.UserNotFoundException(authorId));
        ArticleEntity savedArticle = articlesRepository.save(ArticleEntity.builder()
                .title(request.getTitle())
                // TODO: Create a proper slugification function
                .slug(request.getTitle()
                        .toLowerCase()
                        .replaceAll("\\s+", "-"))
                .body(request.getBody())
                .subtitle(request.getSubtitle())
                .author(author)
                .build());
        return ArticleResponse.builder()
                .id(savedArticle.getId())
                .title(savedArticle.getTitle())
                .slug(savedArticle.getSlug())
                .body(savedArticle.getBody())
                .subtitle(savedArticle.getSubtitle())
                .author(savedArticle.getAuthor().getUsername())
                .build();
    }

    @PreAuthorize("@articleSecurity.isOwner(#articleId, #authentication.principal.id)")
    public ArticleEntity updateArticleById(Long articleId,
            UpdateArticleRequest articleRequest) {
        var article = articlesRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException(articleId));
        if (articleRequest.getTitle() != null) {
            article.setTitle(articleRequest.getTitle());
            article.setSlug(articleRequest.getTitle()
                    .toLowerCase()
                    .replaceAll("\\s+", "-"));
        }
        if (articleRequest.getBody() != null) {
            article.setBody(articleRequest.getBody());
        }
        if (articleRequest.getSubtitle() != null) {
            article.setSubtitle(articleRequest.getSubtitle());
        }
        return articlesRepository.save(article);
    }

    @PreAuthorize("@articleSecurity.isOwner(#slug, authentication.principal.id)")
    public ArticleResponse updateArticleBySlug(String slug, UpdateArticleRequest articleRequest) {
        ArticleEntity article = articlesRepository.findBySlug(slug);
        if (article == null) {
            throw new ArticleNotFoundException(slug);
        }
        if (articleRequest.getTitle() != null) {
            article.setTitle(articleRequest.getTitle());
            article.setSlug(articleRequest.getTitle()
                    .toLowerCase()
                    .replaceAll("\\s+", "-"));
        }
        if (articleRequest.getBody() != null) {
            article.setBody(articleRequest.getBody());
        }
        if (articleRequest.getSubtitle() != null) {
            article.setSubtitle(articleRequest.getSubtitle());
        }
        var saved = articlesRepository.save(article);
        return ArticleResponse.builder()
                .id(saved.getId())
                .author(saved.getAuthor().getUsername())
                .title(saved.getTitle())
                .slug(saved.getSlug())
                .subtitle(saved.getSubtitle())
                .body(saved.getBody())
                .build();
    }

    public void deleteArticle(Long articleId) {
        var article = articlesRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException(articleId));
        articlesRepository.delete(article);
    }

    static class ArticleNotFoundException extends IllegalArgumentException {
        public ArticleNotFoundException(String slug) {
            super("Article with slug: " + slug + " not found");
        }

        public ArticleNotFoundException(Long articleId) {
            super("Article with id: " + articleId + " not found");
        }
    }
}
