package ru.itis.services.impl;

import ru.itis.exceptions.DatabaseException;
import ru.itis.models.FileInfo;
import ru.itis.repositories.files.FileRepository;
import ru.itis.services.FileService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class FileServiceImpl implements FileService {

    private FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository) { this.fileRepository = fileRepository; }

    @Override
    public void saveFileToStorage(InputStream file, Integer fileId, String originalFileName, String contentType, Long size) {
        FileInfo fileInfo = FileInfo.builder()
                .id(fileId)
                .originalFileName(originalFileName)
                .storageFileName(UUID.randomUUID().toString())
                .size(size)
                .type(contentType)
                .build();

        try {
            Files.copy(file, Paths.get("C://files/" + fileInfo.getStorageFileName() + "." + fileInfo.getType().split("/")[1]));
            fileRepository.save(fileInfo);
        } catch (IOException e) {
            throw new IllegalStateException();
        } catch (DatabaseException e) {
            throw new DatabaseException("Не удалось сохранить файл", e);
        }
    }

    @Override
    public void writeFileFromStorage(Integer fileId, OutputStream outputStream) {
        FileInfo fileInfo = fileRepository.findById(fileId);
        File file = new File("C://files/" + fileInfo.getStorageFileName() + "." + fileInfo.getType().split("/")[1]);
        try {
            Files.copy(file.toPath(), outputStream);
        } catch (IOException e) {
            throw new DatabaseException("Не удалось сохранить файл");
        }
    }

    @Override
    public FileInfo getFileInfo(Integer fileId) {
        return fileRepository.findById(fileId);
    }
}
