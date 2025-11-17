package ru.itis.repositories.character;

import java.util.List;
import ru.itis.models.CharacterModel;
import ru.itis.repositories.CrudRepository;

public interface CharacterRepository extends CrudRepository<CharacterModel> {

    List<CharacterModel> findAllByAnimeId(Integer animeId);
}
