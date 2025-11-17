package ru.itis.repositories.anime.impl;

import ru.itis.dto.AnimeDto;
import ru.itis.dto.AnimeFilter;
import ru.itis.exceptions.DatabaseException;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.models.Anime;
import ru.itis.models.Studio;
import ru.itis.repositories.anime.AnimeRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnimeRepositoryImpl implements AnimeRepository {
    private DataSource dataSource;

    private static String SQL_INSERT_INTO_ANIME = "INSERT INTO anime(title, description, release_year, studio_id, added_by) VALUES(?, ?, ?, ?, ?)";

    private static String SQL_SELECT_ALL_ANIME_FROM_ANIME = "SELECT anime_id, title, description, release_year, rating, studio_id, added_by FROM anime";

    private static String SQL_SELECT_ALL_ANIME_BY_ID = "SELECT anime_id, title, description, release_year, rating, studio_id, added_by FROM anime WHERE anime_id = ?";

    private static String SQL_SELECT_ALL_ANIME_BY_TITLE = "SELECT anime_id, title, description, release_year, rating, studio_id, added_by FROM anime WHERE title = ?";

    private static String SQL_UPDATE_SCORE = "UPDATE user_anime " +
            "SET score = ? " +
            "WHERE anime_id = ? AND user_id = ?";

    private static String SQL_COUNT_RATING = "UPDATE anime " +
            "SET rating = ( " +
            "    SELECT AVG(ua.score) " +
            "    FROM user_anime ua " +
            "    WHERE ua.anime_id = anime.anime_id " +
            ") " +
            "WHERE anime_id = ?";

    private static String SQL_SELECT_ANIME_ID = "SELECT anime_id " +
            "FROM anime " +
            "WHERE title = ?";

    public AnimeRepositoryImpl(DataSource dataSource){ this.dataSource = dataSource; }

    @Override
    public void save(Anime anime){
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_INTO_ANIME)){
            preparedStatement.setString(1, anime.getTitle());
            preparedStatement.setString(2, anime.getDescription());
            preparedStatement.setInt(3, anime.getReleaseYear());
            preparedStatement.setInt(4, anime.getStudioId());
            preparedStatement.setInt(5, anime.getUserId());
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            throw new DatabaseException("Не удалось сохранить аниме: " + anime.getTitle(), e);
        }
    }

    @Override
    public List<Anime> findAll(){
        List<Anime> animeList = new ArrayList<>();

        try (Connection connection =  dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL_ANIME_FROM_ANIME)){

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Anime anime = Anime.builder()
                        .id(resultSet.getInt("anime_id"))
                        .title(resultSet.getString("title"))
                        .description(resultSet.getString("description"))
                        .releaseYear(resultSet.getInt("release_year"))
                        .rating(resultSet.getDouble("rating"))
                        .studioId(resultSet.getInt("studio_id"))
                        .build();

                animeList.add(anime);
            }
        } catch (SQLException e){
            throw new DatabaseException("Не удалось найти все аниме", e);
        }

        return animeList;
    }

    public List<Anime> findByFilter(AnimeFilter filter){
        List<Anime> animeList = new ArrayList<>();

        StringBuilder SQL_SELECT_ANIME_BY_FILTER = new StringBuilder(
                "SELECT a.anime_id, a.title, a.description, a.release_year, a.rating, a.studio_id, a.added_by " +
                        "FROM anime a " +
                        "WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();

        if(filter.getTitle() != null && !filter.getTitle().isEmpty()){
            SQL_SELECT_ANIME_BY_FILTER.append(" AND a.title ILIKE ?");
            params.add("%" + filter.getTitle().trim() + "%");
        }

        if (filter.getReleaseYear() != null) {
            SQL_SELECT_ANIME_BY_FILTER.append(" AND a.release_year = ?");
            params.add(filter.getReleaseYear());
        }

        if (filter.getGenre() != null && !filter.getGenre().trim().isEmpty()) {
            SQL_SELECT_ANIME_BY_FILTER.append(" AND EXISTS (SELECT 1 FROM anime_genre ag " +
                    "JOIN genre g ON ag.genre_id = g.genre_id " +
                    "WHERE ag.anime_id = a.anime_id AND g.name ILIKE ?)");
            params.add("%" + filter.getGenre().trim() + "%");
        }

        if (filter.getStudio() != null && !filter.getStudio().trim().isEmpty()) {
            SQL_SELECT_ANIME_BY_FILTER.append(" AND EXISTS (SELECT 1 FROM studio s WHERE s.studio_id = a.studio_id AND s.name ILIKE ?)");
            params.add("%" + filter.getStudio().trim() + "%");
        }

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ANIME_BY_FILTER.toString())){

            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof Integer) {
                    statement.setInt(i + 1, (Integer) param);
                } else if (param instanceof String) {
                    statement.setString(i + 1, (String) param);
                }
            }

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Anime anime = Anime.builder()
                        .id(resultSet.getInt("anime_id"))
                        .title(resultSet.getString("title"))
                        .description(resultSet.getString("description"))
                        .releaseYear(resultSet.getInt("release_year"))
                        .rating(resultSet.getDouble("rating"))
                        .studioId(resultSet.getInt("studio_id"))
                        .build();

                animeList.add(anime);
            }


        } catch (SQLException e){
            throw new DatabaseException("Не удалось найти аниме по фильтрам", e);
        }

        return animeList;
    }


    @Override
    public Anime findById(Integer id) {
        Anime anime = null;

        try (Connection connection =  dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL_ANIME_BY_ID)){

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                anime = Anime.builder()
                        .id(resultSet.getInt("anime_id"))
                        .title(resultSet.getString("title"))
                        .description(resultSet.getString("description"))
                        .releaseYear(resultSet.getInt("release_year"))
                        .rating(resultSet.getDouble("rating"))
                        .studioId(resultSet.getInt("studio_id"))
                        .build();

            }
        } catch (SQLException e){
            throw new DatabaseException("Не удалось найти аниме с идентификатором: " + id, e);
        }

        return anime;
    }

    @Override
    public Anime findByTitle(String title) {
        Anime anime = null;

        try (Connection connection =  dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL_ANIME_BY_TITLE)){

            statement.setString(1, title.trim());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                anime = Anime.builder()
                        .id(resultSet.getInt("anime_id"))
                        .title(resultSet.getString("title"))
                        .description(resultSet.getString("description"))
                        .releaseYear(resultSet.getInt("release_year"))
                        .rating(resultSet.getDouble("rating"))
                        .studioId(resultSet.getInt("studio_id"))
                        .build();

            }
        } catch (SQLException e){
            throw new DatabaseException("Не удалось найти аниме с названием: " + title, e);
        }

        return anime;
    }

    @Override
    public void countRating(Integer animeId, Integer score, Integer userId){
        try(Connection connection = dataSource.getConnection();
            PreparedStatement countRating = connection.prepareStatement(SQL_COUNT_RATING);
            PreparedStatement updateScore = connection.prepareStatement(SQL_UPDATE_SCORE)){

            updateScore.setInt(1, score);
            updateScore.setInt(2, animeId);
            updateScore.setInt(3, userId);
            updateScore.executeUpdate();

            countRating.setInt(1, animeId);
            countRating.executeUpdate();

        } catch (SQLException e){
            throw new DatabaseException("Не удалось посчитать рейтинг для аниме с идентификатором: " + animeId, e);
        }
    }

    public void reCalculateRating(Integer animeId){
        try(Connection connection = dataSource.getConnection();
            PreparedStatement countRating = connection.prepareStatement(SQL_COUNT_RATING)){

            countRating.setInt(1, animeId);
            countRating.executeUpdate();

        } catch (SQLException e){
            throw new DatabaseException("Не удалось пересчитать рейтинг для аниме с идентификатором: " + animeId, e);
        }
    }

    @Override
    public Integer getAnimeId(String title) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ANIME_ID)){
            statement.setString(1, title);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                return resultSet.getInt("anime_id");
            }

        } catch (SQLException e){
            throw new DatabaseException("Не удалось найти аниме с названием " + title);
        }

        return null;
    }
}
