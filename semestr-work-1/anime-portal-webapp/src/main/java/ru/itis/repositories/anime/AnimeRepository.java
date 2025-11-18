package ru.itis.repositories.anime;

import ru.itis.dto.AnimeDto;
import ru.itis.dto.AnimeFilter;
import ru.itis.models.Anime;
import ru.itis.models.Studio;
import ru.itis.repositories.CrudRepository;

import java.sql.SQLException;
import java.util.List;

public interface AnimeRepository extends CrudRepository<Anime> {

    List<Anime> findByFilter(AnimeFilter filter);

    Anime findById(Integer id);

    Anime findByTitle(String title);

    void countRating(Integer animeId, Integer score, Integer userId);

    void reCalculateRating(Integer animeId);

    Integer getAnimeId(String title);
}
