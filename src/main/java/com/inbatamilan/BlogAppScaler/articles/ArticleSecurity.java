package com.inbatamilan.BlogAppScaler.articles;

import org.springframework.stereotype.Component;

@Component("articleSecurity")
public class ArticleSecurity {

    private final ArticlesRepository articlesRepository;

    public ArticleSecurity(ArticlesRepository articlesRepository) {
        this.articlesRepository = articlesRepository;
    }

    public boolean isOwner(Long articleId, Long currentUserId) {
        ArticleEntity article = articlesRepository.findById(articleId).orElseThrow(() ->
                 new ArticlesService.ArticleNotFoundException(articleId)
        );
        return article.getAuthor().getId().equals(currentUserId);
    }

    public boolean isOwner(String articleSlug, Long currentUserId) {
        ArticleEntity article = articlesRepository.findBySlug(articleSlug);
        if (article == null) {
            throw new ArticlesService.ArticleNotFoundException(articleSlug);
        }
        return article.getAuthor().getId().equals(currentUserId);
    }
}
