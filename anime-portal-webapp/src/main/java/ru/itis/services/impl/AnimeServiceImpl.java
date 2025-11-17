package ru.itis.services.impl;

import ru.itis.dto.AnimeDto;
import ru.itis.dto.AnimeFilter;
import ru.itis.models.Anime;
import ru.itis.repositories.anime.AnimeRepository;
import ru.itis.repositories.genres.GenresRepository;
import ru.itis.repositories.studio.StudioRepository;
import ru.itis.services.AnimeService;

import java.util.List;
import java.util.stream.Collectors;

public class AnimeServiceImpl implements AnimeService {

    private final AnimeRepository animeRepository;
    private final StudioRepository studioRepository;
    private final GenresRepository genresRepository;

    public AnimeServiceImpl(AnimeRepository animeRepository, StudioRepository studioRepository, GenresRepository genresRepository){
        this.animeRepository = animeRepository;
        this.studioRepository = studioRepository;
        this.genresRepository = genresRepository;
    }

    public List<AnimeDto> getAnimeList(AnimeFilter filter) {
        List<Anime> animeList = animeRepository.findByFilter(filter);
        return animeList.stream()
                .map(anime -> AnimeDto.builder()
                        .id(anime.getId())
                        .title(anime.getTitle())
                        .releaseYear(anime.getReleaseYear())
                        .description(anime.getDescription())
                        .rating(anime.getRating())
                        .genres(genresRepository.getGenresByAnimeId(anime.getId()))
                        .studio(studioRepository.getStudio(anime.getStudioId()))
                        .build())
                .collect(Collectors.toList());
    }

    public List<AnimeDto> getAllAnimeList(){
        return getAnimeList(AnimeFilter.builder().build());
    }


}
