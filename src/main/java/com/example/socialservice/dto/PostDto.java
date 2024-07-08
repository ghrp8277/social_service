package com.example.socialservice.dto;

import com.example.socialservice.entity.Post;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostDto {
    private Long id;
    private String accountName;
    private String author;
    private String content;
    private String createdAt;
    private int likes;
    private String title;
    private Long userId;
    private int views;
    private List<CommentDto> comments;

    public static PostDto fromEntity(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setAccountName(post.getAccountName());
        dto.setAuthor(post.getAuthor());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt().toString());
        dto.setLikes(post.getLikes());
        dto.setTitle(post.getTitle());
        dto.setUserId(post.getUserId());
        dto.setViews(post.getViews());
        return dto;
    }

    public static PostDto fromEntityWithComments(Post post) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        List<CommentDto> commentDtos = post.getComments().stream()
            .map(CommentDto::fromEntity)
            .collect(Collectors.toList());

        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setAccountName(post.getAccountName());
        dto.setAuthor(post.getAuthor());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt().format(formatter));
        dto.setLikes(post.getLikes());
        dto.setTitle(post.getTitle());
        dto.setUserId(post.getUserId());
        dto.setViews(post.getViews());
        dto.setComments(commentDtos);
        return dto;
    }
}

