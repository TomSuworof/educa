package com.dreamteam.eduuca.repositories;

import com.dreamteam.eduuca.entities.article.Article;
import com.dreamteam.eduuca.entities.article.like.Like;
import com.dreamteam.eduuca.entities.article.like.LikeId;
import com.dreamteam.eduuca.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Like, LikeId> {
    List<Like> findAllByUser(User user);

    List<Like> findAllByArticle(Article article);

    Integer countByArticle_Id(UUID articleID);
}
