package ru.itis.repositories.genres;

import java.util.List;

public interface GenresRepository{

    void addGenresToAnime(Integer genreId, Integer animeId);

    void addGenre(String genreName);

    Integer getGenreId(String genreName);

    List<String> getAllGenres();

    List<String> getGenresByAnimeId(Integer animeId);
}
