package com.dreamteam.eduuca.entities.progress;

import com.dreamteam.eduuca.entities.article.Article;
import com.dreamteam.eduuca.entities.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@Table(name = "t_progress")
@IdClass(ProgressId.class)
@NoArgsConstructor
public class Progress {
    @Id
    @Column(name = "user_id")
    private UUID userID;

    @Id
    @Column(name = "article_id")
    private UUID articleID;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    private Article article;

    @Column
    private ProgressEnum progress;
}
