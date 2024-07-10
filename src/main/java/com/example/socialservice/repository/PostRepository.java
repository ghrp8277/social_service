package com.example.socialservice.repository;

import com.example.socialservice.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @EntityGraph(attributePaths = {"comments", "comments.replies"})
    Optional<Post> findById(Long id);
    @Query("SELECT p FROM Post p")
    Page<Post> findAllPosts(Pageable pageable);
    Page<Post> findByTitleContainingOrAuthorContainingOrContentContaining(String title, String author, String content, Pageable pageable);
}