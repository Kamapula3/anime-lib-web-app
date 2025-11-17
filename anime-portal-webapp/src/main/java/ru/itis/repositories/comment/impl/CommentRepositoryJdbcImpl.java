package ru.itis.repositories.comment.impl;

import ru.itis.exceptions.DatabaseException;
import ru.itis.models.Comment;
import ru.itis.models.User;
import ru.itis.repositories.comment.CommentRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentRepositoryJdbcImpl implements CommentRepository {

    private static String SQL_INSERT_INTO_COMMENT = "INSERT INTO comment(user_id, anime_id, text) VALUES(?, ?, ?)";

    private static String SQL_SELECT_ALL_ANIME_COMMENTS = "SELECT comment_id, user_id, anime_id, created_at, text " +
            "FROM comment " +
            "WHERE anime_id = ?";

    private static String SQL_DELETE_COMMENT = "DELETE FROM comment " +
            "WHERE comment_id = ? AND user_id = ? AND anime_id = ?";

    private static String SQL_SELECT_COMMENT = "SELECT comment_id, user_id, anime_id, created_at, text " +
            "FROM comment " +
            "WHERE anime_id = ? AND user_id = ?";

    private static String SQL_SELECT_COMMENTATOR_NAME = "SELECT u.username " +
            "FROM user_account u " +
            "JOIN comment c ON c.user_id = u.user_id " +
            "WHERE u.user_id = ?";


    private DataSource dataSource;

    public CommentRepositoryJdbcImpl(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public void save(Comment comment) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_INTO_COMMENT)){
            preparedStatement.setInt(1, comment.getUserId());
            preparedStatement.setInt(2, comment.getAnimeId());
            preparedStatement.setString(3, comment.getText());
            preparedStatement.executeUpdate();

        } catch (SQLException e){
            throw new DatabaseException("Не удалось сохранить комментарий", e);
        }
    }

    @Override
    public List<Comment> findAll() {
        return null;
    }

    @Override
    public Comment getCommentById(Integer animeId, Integer userId) {

        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_SELECT_COMMENT)){

            statement.setInt(1, animeId);
            statement.setInt(2, userId);

            System.out.println("TEST: " + animeId + " " + userId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Comment.builder()
                        .id(resultSet.getInt("comment_id"))
                        .userId(resultSet.getInt("user_id"))
                        .animeId(resultSet.getInt("anime_id"))
                        .text(resultSet.getString("text"))
                        .createdAt(resultSet.getString("created_at"))
                        .build();
            }

        } catch (SQLException e) {
            throw new DatabaseException("Не удалось удалить комментарий", e);
        }

        return null;
    }

    @Override
    public List<Comment> findAllByAnimeId(Integer commentId) {
        List<Comment> comments = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_ANIME_COMMENTS)){

            preparedStatement.setInt(1, commentId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Comment comment = Comment.builder()
                        .id(resultSet.getInt("comment_id"))
                        .userId(resultSet.getInt("user_id"))
                        .animeId(resultSet.getInt("anime_id"))
                        .text(resultSet.getString("text"))
                        .createdAt(resultSet.getString("created_at"))
                        .build();
                comments.add(comment);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Не удалось найти всех пользователей", e);
        }
        return comments;
    }

    @Override
    public void removeById(Integer commentId, Integer userId, Integer animeId) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE_COMMENT)){

            statement.setInt(1, commentId);
            statement.setInt(2, userId);
            statement.setInt(3, animeId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Не удалось удалить комментарий", e);
        }
    }

    @Override
    public String getCommentatorName(Integer userId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_COMMENTATOR_NAME)){

            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return resultSet.getString("username");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Не удалось найти пользователя с идентификатором" + userId, e);
        }

        return null;
    }
}
