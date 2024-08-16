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
        return fromEntityWithComments(post, false);
    }

    public static PostDto fromEntityWithComments(Post post) {
        return fromEntityWithComments(post, true);
    }

    private static PostDto fromEntityWithComments(Post post, boolean includeComments) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

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

        if (includeComments && post.getComments() != null) {
            List<CommentDto> commentDtos = post.getComments().stream()
                    .map(CommentDto::fromEntity)
                    .collect(Collectors.toList());
            dto.setComments(commentDtos);
        }

        return dto;
    }
}


