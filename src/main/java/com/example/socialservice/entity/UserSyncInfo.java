package com.example.socialservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user_sync_info")
public class UserSyncInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private LocalDateTime lastSyncedAt;

    @Column(nullable = false)
    private boolean active;

    @PrePersist
    protected void onCreate() {
        this.lastSyncedAt = LocalDateTime.now();
        this.active = true;
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastSyncedAt = LocalDateTime.now();
    }
}
