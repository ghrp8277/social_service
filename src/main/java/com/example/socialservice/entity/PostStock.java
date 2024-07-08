package com.example.socialservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "post_stock", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"stock_code", "stock_name"})
})
public class PostStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_code", nullable = false)
    private Long stockCode;

    @Column(name = "stock_name", nullable = false)
    private String stockName;

    @Column(name = "market", nullable = false)
    private String market;

    @OneToMany(mappedBy = "postStock", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Post> posts;
}
