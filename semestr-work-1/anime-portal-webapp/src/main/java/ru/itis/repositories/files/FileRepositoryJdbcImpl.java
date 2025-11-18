package ru.itis.repositories.files;

import ru.itis.exceptions.DatabaseException;
import ru.itis.models.FileInfo;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class FileRepositoryJdbcImpl implements FileRepository{

    private DataSource dataSource;

    private static String SQL_INSERT_INTO_ANIME_IMAGE = "INSERT INTO anime_image(id, storage_file_name, original_file_name, type, size) VALUES(?, ?, ?, ?, ?)";

    private static String SQL_SELECT_FILE_BY_ID = "SELECT id, storage_file_name, original_file_name, type, size " +
            "FROM anime_image " +
            "WHERE id = ?";

    public FileRepositoryJdbcImpl(DataSource dataSource){ this.dataSource = dataSource; }


    @Override
    public FileInfo findById(Integer id) {
        FileInfo fileInfo = null;

        try (Connection connection =  dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_FILE_BY_ID)){

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                fileInfo = FileInfo.builder()
                        .id(resultSet.getInt("id"))
                        .storageFileName(resultSet.getString("storage_file_name"))
                        .originalFileName(resultSet.getString("original_file_name"))
                        .type(resultSet.getString("type"))
                        .size(resultSet.getLong("size"))
                        .build();

            }
        } catch (SQLException e){
            throw new DatabaseException("Не удалось найти аниме с идентификатором: " + id, e);
        }

        return fileInfo;
    }


    @Override
    public void save(FileInfo file) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT_INTO_ANIME_IMAGE)){

            statement.setInt(1, file.getId());
            statement.setString(2, file.getStorageFileName());
            statement.setString(3, file.getOriginalFileName());
            statement.setString(4, file.getType());
            statement.setLong(5, file.getSize());

            statement.executeUpdate();
        } catch (SQLException e){
            throw new DatabaseException("Не удалось сохранить файл: " + file.getOriginalFileName(), e);
        }
    }

    @Override
    public List<FileInfo> findAll() {
        return null;
    }
}
