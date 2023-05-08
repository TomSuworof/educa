package com.dreamteam.eduuca.entities.progress;

import com.dreamteam.eduuca.entities.article.Article;
import com.dreamteam.eduuca.entities.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @MapsId
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @MapsId
    @JoinColumn(name = "article_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Article article;

    @Column
    private ProgressEnum progress;
}
