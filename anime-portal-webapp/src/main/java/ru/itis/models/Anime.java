package ru.itis.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Anime {
    private Integer id;
    private String title;
    private String description;
    private Integer releaseYear;
    private Double rating;
    private Integer userId;
    private Integer studioId;
}
