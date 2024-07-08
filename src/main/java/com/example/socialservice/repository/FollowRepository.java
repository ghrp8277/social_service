package com.example.socialservice.repository;

import com.example.socialservice.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerIdAndFolloweeId(Long followerId, Long followeeId);
    void deleteByFollowerIdAndFolloweeId(Long followerId, Long followeeId);
    List<Follow> findByFolloweeId(Long followeeId);
    List<Follow> findByFollowerId(Long followerId);
    Optional<Follow> findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);
}
