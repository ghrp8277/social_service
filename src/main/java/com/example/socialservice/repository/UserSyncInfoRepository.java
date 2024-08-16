package com.example.socialservice.repository;

import com.example.socialservice.entity.UserSyncInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface UserSyncInfoRepository extends JpaRepository<UserSyncInfo, Long> {
    @Query("SELECT u FROM UserSyncInfo u LEFT JOIN Follow f ON u.userId = f.followeeId AND f.followerId = :followerId WHERE f.id IS NULL AND u.userId <> :followerId")
    Page<UserSyncInfo> findUsersNotFollowedBy(@Param("followerId") Long followerId, Pageable pageable);
}
