package com.example.socialservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "views", nullable = false)
    private int views = 0;

    @Column(name = "likes", nullable = false)
    private int likes = 0;

    @Column(name = "account_name", nullable = false)
    private String accountName;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Comment> comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_stock_id", nullable = false)
    private PostStock postStock;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public void incrementViews() {
        this.views += 1;
    }

    public void incrementLikes() {
        this.likes += 1;
    }

    public void decrementLikes() {
        if (this.likes > 0) {
            this.likes -= 1;
        }
    }
}