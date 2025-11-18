package ru.itis.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDto {
    private Integer id;
    private Integer userId;
    private String username;
    private Integer animeId;
    private String text;
    private String createdAt;
}
