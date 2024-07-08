package com.example.socialservice.dto;

import com.example.socialservice.entity.Comment;
import lombok.Data;
import java.time.format.DateTimeFormatter;

@Data
public class CommentDto {
    private Long id;
    private String content;
    private String createdAt;
    private int likes;
    private Long parentCommentId;
    private Long postId;
    private Long userId;

    public static CommentDto fromEntity(Comment comment) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt().format(formatter));
        dto.setLikes(comment.getLikes());
        dto.setParentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null);
        dto.setPostId(comment.getPost().getId());
        dto.setUserId(comment.getUserId());
        return dto;
    }
}