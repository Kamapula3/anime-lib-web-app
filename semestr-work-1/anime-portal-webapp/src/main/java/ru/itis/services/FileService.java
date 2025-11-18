package ru.itis.services;

import ru.itis.models.FileInfo;

import java.io.InputStream;
import java.io.OutputStream;

public interface FileService {
    void saveFileToStorage(InputStream file, Integer fileId, String originalFileName, String contentType, Long size);

    void writeFileFromStorage(Integer fileId, OutputStream outputStream);

    FileInfo getFileInfo(Integer fileId);
}
