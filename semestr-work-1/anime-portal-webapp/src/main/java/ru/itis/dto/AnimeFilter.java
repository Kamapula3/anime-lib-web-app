package ru.itis.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AnimeFilter {
    private String title;
    private Integer releaseYear;
    private String genre;
    private String studio;
}
