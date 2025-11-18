package ru.itis.repositories.studio;

import ru.itis.models.Studio;
import ru.itis.repositories.CrudRepository;

public interface StudioRepository extends CrudRepository<Studio> {
    Integer getStudioId(String studioName);

    Studio getStudio(Integer studioId);
}
