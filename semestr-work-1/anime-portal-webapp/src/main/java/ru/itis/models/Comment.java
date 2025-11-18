package ru.itis.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private Integer id;
    private Integer userId;
    private Integer animeId;
    private String text;
    private String createdAt;
}
