package com.inbatamilan.BlogAppScaler.articles;

import com.inbatamilan.BlogAppScaler.articles.dtos.CreateArticleRequest;
import com.inbatamilan.BlogAppScaler.articles.dtos.UpdateArticleRequest;
import com.inbatamilan.BlogAppScaler.users.UserRepository;
import com.inbatamilan.BlogAppScaler.users.UsersService;
import org.springframework.stereotype.Service;

@Service
public class ArticlesService {

    private final ArticlesRepository articlesRepository;
    private final UserRepository userRepository;

    public ArticlesService(ArticlesRepository articlesRepository,
            UserRepository userRepository) {
        this.articlesRepository = articlesRepository;
        this.userRepository = userRepository;
    }

    public Iterable<ArticleEntity> getAllArticles() {
        return articlesRepository.findAll();
    }

    public ArticleEntity getArticleBySlug(String slug) {
        var article = articlesRepository.findBySlug(slug);
        if (article == null) {
            throw new ArticleNotFoundException(slug);
        }
        return article;
    }

    public ArticleEntity createArticle(CreateArticleRequest request,
            Long authorId) {
        var author = userRepository.findById(authorId)
                .orElseThrow(() ->
                        new UsersService.UserNotFoundException(authorId));
        return articlesRepository.save(ArticleEntity.builder()
                .title(request.getTitle())
                // TODO: Create a proper slugification function
                .slug(request.getTitle()
                        .toLowerCase()
                        .replaceAll("\\s+", "-"))
                .body(request.getBody())
                .subtitle(request.getSubtitle())
                .author(author)
                .build());
    }

    public ArticleEntity updateArticle(Long articleId,
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
