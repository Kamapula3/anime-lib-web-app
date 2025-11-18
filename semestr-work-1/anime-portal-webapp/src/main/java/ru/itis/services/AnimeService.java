package ru.itis.services;

import ru.itis.dto.AnimeDto;
import ru.itis.dto.AnimeFilter;

import java.util.List;

public interface AnimeService {
    List<AnimeDto> getAnimeList(AnimeFilter filter);

    List<AnimeDto> getAllAnimeList();
}
