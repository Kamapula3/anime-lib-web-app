package ru.itis.dto;

import lombok.Builder;
import lombok.Data;
import ru.itis.models.Studio;

import java.util.List;

@Data
@Builder
public class AnimeDto {
    private Integer id;
    private String title;
    private String description;
    private Integer releaseYear;
    private Double rating;
    private Studio studio;
    private String status;
    private List<String> genres;
    private Integer coverFileId;
}
