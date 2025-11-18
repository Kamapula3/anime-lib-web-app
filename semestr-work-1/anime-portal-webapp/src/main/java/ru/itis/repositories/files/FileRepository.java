package ru.itis.repositories.files;

import ru.itis.models.FileInfo;
import ru.itis.repositories.CrudRepository;

public interface FileRepository extends CrudRepository<FileInfo> {
    FileInfo findById(Integer id);
}

