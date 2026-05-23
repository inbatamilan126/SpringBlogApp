package com.inbatamilan.BlogAppScaler.comments;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<CommentEntity, Long> {
    Page<CommentEntity> getCommentEntitiesByArticle_Slug(String articleSlug, Pageable pageable);
}
