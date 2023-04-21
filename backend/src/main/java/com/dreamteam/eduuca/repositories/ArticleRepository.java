package com.dreamteam.eduuca.repositories;

import com.dreamteam.eduuca.entities.Article;
import com.dreamteam.eduuca.entities.ArticleState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ArticleRepository<T extends Article> extends JpaRepository<T, UUID> {
    Optional<T> findByCustomUrl(String customUrl);

    Page<T> findByState(ArticleState state, Pageable pageable);

    Page<T> findByStateAndTags_IdIn(ArticleState state, Set<UUID> tags_id, Pageable pageable);

    @Query(value = "select * from t_article where to_tsvector(title || ' ' || custom_url || ' ' || summary || ' ' || content || ' ' || solution)  @@ to_tsquery( :query );", nativeQuery = true)
    List<T> fullTextSearch(String query);
}
