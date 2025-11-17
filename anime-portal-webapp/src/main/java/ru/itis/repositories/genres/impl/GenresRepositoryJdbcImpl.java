package ru.itis.repositories.genres.impl;

import ru.itis.exceptions.DatabaseException;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.repositories.genres.GenresRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenresRepositoryJdbcImpl implements GenresRepository {

    private DataSource dataSource;

    private static String SQL_SELECT_GENRES = "SELECT g.name " +
            "FROM anime a " +
            "JOIN anime_genre ag ON ag.anime_id = a.anime_id " +
            "JOIN genre g ON ag.genre_id = g.genre_id " +
            "WHERE a.anime_id = ?";

    private static String SQL_SELECT_ALL_GENRES = "SELECT name " +
            "FROM genre";

    private static String SQL_SELECT_GENRE_ID = "SELECT genre_id FROM genre " +
            "WHERE name = ?";

    private static String SQL_INSERT_GENRE_TO_ANIME = "INSERT INTO anime_genre(anime_id, genre_id) " +
            "VALUES(?, ?)";

    private static String SQL_INSERT_GENRE = "INSERT INTO genre(name) " +
            "VALUES(?)";

    public GenresRepositoryJdbcImpl(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public List<String> getGenresByAnimeId(Integer animeId){
        List<String> genres = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_GENRES)){

            statement.setInt(1, animeId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                String genreName = resultSet.getString("name");
                genres.add(genreName);
            }
        } catch (SQLException e){
            throw new DatabaseException("Не удалось найти жанры для аниме с идентификаторм: " + animeId, e);
        }

        return genres;
    }

    @Override
    public List<String> getAllGenres() {
        List<String> genres = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL_GENRES)){


            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                String genreName = resultSet.getString("name");
                genres.add(genreName);
            }
        } catch (SQLException e){
            throw new DatabaseException("Не удалось найти жанры", e);
        }

        return genres;
    }

    @Override
    public Integer getGenreId(String genreName){
        Integer genreId = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_GENRE_ID)){
            preparedStatement.setString(1, genreName.trim());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                genreId = resultSet.getInt("genre_id");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Не удалось найти идентификатор жанра", e);
        }

        return genreId;
    }

    @Override
    public void addGenresToAnime(Integer genreId, Integer animeId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_GENRE_TO_ANIME)){
            preparedStatement.setInt(1, animeId);
            preparedStatement.setInt(2, genreId);

            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            throw new DatabaseException("Не удалось найти идентификатор жанра", e);
        }
    }

    @Override
    public void addGenre(String genreName) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_GENRE)){

            statement.setString(1, genreName);

            statement.executeUpdate();

        } catch (SQLException e){
            throw new DatabaseException("Не удалось сохранить жанр", e);
        }
    }
}
