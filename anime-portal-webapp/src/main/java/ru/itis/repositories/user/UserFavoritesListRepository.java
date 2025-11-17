package ru.itis.repositories.user;

import ru.itis.dto.AnimeDto;
import ru.itis.exceptions.DatabaseException;
import ru.itis.repositories.anime.AnimeRepository;
import ru.itis.repositories.genres.GenresRepository;
import ru.itis.repositories.studio.StudioRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserFavoritesListRepository {

    private DataSource dataSource;
    private AnimeRepository animeRepository;
    private StudioRepository studioRepository;
    private GenresRepository genresRepository;

    private static String SQL_JOIN_USER_ANIME = "SELECT a.anime_id, a.title, a.description, a.release_year, a.rating, a.studio_id " +
            "FROM user_account u " +
            "JOIN user_anime ua ON ua.user_id = u.user_id " +
            "JOIN anime a ON a.anime_id = ua.anime_id " +
            "WHERE u.user_id = ?";

    private static String SQL_INSERT_ANIME_TO_FAVORITE = "INSERT INTO user_anime(user_id, anime_id) " +
            "VALUES (?, ?) " +
            "ON CONFLICT (user_id, anime_id) DO NOTHING;";

    private static String SQL_CHEK_IN_FAVORITE = "SELECT 1 " +
            "FROM user_anime " +
            "WHERE user_id = ? AND anime_id = ?";

    private static String SQL_SELECT_SCORE = "SELECT score " +
            "FROM user_anime " +
            "WHERE user_id = ? AND anime_id = ?";

    private static String SQL_SELECT_STATUS = "SELECT status " +
            "FROM user_anime " +
            "WHERE user_id = ? AND anime_id = ?";

    private static String SQL_UPDATE_STATUS = "UPDATE user_anime " +
            "SET status = ? " +
            "WHERE user_id = ? AND anime_id = ?";

    private static String SQL_DELETE_ANIME_FAVORITE = "DELETE FROM user_anime " +
            "WHERE user_id = ? AND anime_id = ?";

    public UserFavoritesListRepository(DataSource dataSource, AnimeRepository animeRepository, StudioRepository studioRepository, GenresRepository genresRepository){
        this.dataSource = dataSource;
        this.animeRepository = animeRepository;
        this.studioRepository = studioRepository;
        this.genresRepository = genresRepository;
    }

    public List<AnimeDto> getFavoritesList(Integer userId) {
        List<AnimeDto> animeList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_JOIN_USER_ANIME)){

            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                AnimeDto anime = AnimeDto.builder()
                        .id(resultSet.getInt("anime_id"))
                        .title(resultSet.getString("title"))
                        .genres(genresRepository.getGenresByAnimeId(resultSet.getInt("anime_id")))
                        .rating(resultSet.getDouble("rating"))
                        .releaseYear(resultSet.getInt("release_year"))
                        .description(resultSet.getString("description"))
                        .studio(studioRepository.getStudio(resultSet.getInt("studio_id"))) //тут
                        .status(getStatus(resultSet.getInt("anime_id"), userId))
                        .build();
                animeList.add(anime);
            }
        } catch (SQLException e){
            throw new DatabaseException("Не удалось найти найти избраные у пользователя с идентификатором:" + userId, e);
        }
        return animeList;
    }

    public Boolean isInFavorite(Integer animeId, Integer userId){
        if (userId == null || animeId == null) {
            return null;
        }

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_CHEK_IN_FAVORITE)){

            statement.setInt(1, userId);
            statement.setInt(2, animeId);

            return statement.executeQuery().next();


        } catch (SQLException e){
            throw new DatabaseException("Ошибка при получении оценки", e);
        }
    }

    public Integer getScore(Integer animeId, Integer userId){
        if (userId == null || animeId == null) {
            return null;
        }

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_SELECT_SCORE)){

            statement.setInt(1, userId);
            statement.setInt(2, animeId);

            ResultSet resultSet =  statement.executeQuery();

            if(resultSet.next()){
                return resultSet.getInt("score");
            }


        } catch (SQLException e){
            throw new DatabaseException("Ошибка при получении оценки", e);
        }

        return null;
    }

    public String getStatus(Integer animeId, Integer userId){
        if (userId == null || animeId == null) {
            return null;
        }

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_SELECT_STATUS)){

            statement.setInt(1, userId);
            statement.setInt(2, animeId);

            ResultSet resultSet =  statement.executeQuery();

            if(resultSet.next()){
                return resultSet.getString("status");
            }


        } catch (SQLException e){
            throw new DatabaseException("Ошибка при получении статуса", e);
        }

        return null;
    }

    public void updateStatus(Integer animeId, Integer userId, String status){

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_STATUS)){

            statement.setString(1, status);
            statement.setInt(2, userId);
            statement.setInt(3, animeId);

            statement.executeUpdate();

        } catch (SQLException e){
            throw new DatabaseException("Не удалось изменить статус", e);
        }
    }

    public void removeAnimeFromFavorite(Integer animeId, Integer userId){

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE_ANIME_FAVORITE)){

            statement.setInt(1, userId);
            statement.setInt(2, animeId);

            statement.executeUpdate();

            animeRepository.reCalculateRating(animeId);


        } catch (SQLException e){
            throw new DatabaseException("Ошибка при удалении из избранного", e);
        }
    }

    public void addAnimeToFavorites(Integer animeId, Integer userId){

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_ANIME_TO_FAVORITE)){

            statement.setInt(1, userId);
            statement.setInt(2, animeId);

            statement.executeUpdate();

        } catch (SQLException e){
            throw new DatabaseException("Не удалось добавить в избранное аниме с идентификатором: " + animeId, e);
        }
    }

}
