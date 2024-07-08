package com.example.socialservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDto {
    private Long id;
    private Long userId;
    private String type;
    private Long referenceId;
    private String message;
    private LocalDateTime createdAt;
    private boolean isRead;
}
