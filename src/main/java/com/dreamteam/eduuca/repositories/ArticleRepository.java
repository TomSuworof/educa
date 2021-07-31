package com.dreamteam.eduuca.repositories;

import com.dreamteam.eduuca.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Article findByFilename(String filename);

    Optional<Article> findArticleByTitle(String title);

}
