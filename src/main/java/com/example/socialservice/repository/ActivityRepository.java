package com.example.socialservice.repository;

import com.example.socialservice.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Page<Activity> findByUserIdInOrderByCreatedAtDesc(List<Long> userIds, Pageable pageable);
    Page<Activity> findByUserIdInAndIsReadFalseOrderByCreatedAtDesc(List<Long> userIds, Pageable pageable);
    Optional<Activity> findTopByUserIdInOrderByCreatedAtDesc(List<Long> userIds);
}
