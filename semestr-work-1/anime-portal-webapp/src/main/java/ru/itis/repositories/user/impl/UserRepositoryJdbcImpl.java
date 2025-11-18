package ru.itis.repositories.user.impl;

import ru.itis.dto.AnimeFilter;
import ru.itis.exceptions.DatabaseException;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.models.Anime;
import ru.itis.models.User;
import ru.itis.repositories.user.UserRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryJdbcImpl implements UserRepository {

    private DataSource dataSource;

    private static String SQL_INSERT_INTO_USER_ACCOUNT = "INSERT INTO user_account(username, email, password) VALUES(?, ?, ?)";

    private static String SQL_ADMIN_INSERT_INTO_USER_ACCOUNT = "INSERT INTO user_account(username, email, password, role_id) VALUES(?, ?, ?, ?)";

    private static String SQL_SELECT_USER_FROM_USER_ACCOUNT_BY_EMAIL = "SELECT user_id, username, email, password, role_id, reg_date FROM user_account WHERE email = ?";

    private static String SQL_SELECT_ALL_USER_FROM_USER_ACCOUNT = "SELECT user_id, username, email, role_id, reg_date FROM user_account";

    private static String SQL_SELECT_USER_FROM_USER_ACCOUNT_BY_USERNAME = "SELECT username, email, password, role_id FROM user_account WHERE username = ?";

    private static String SQL_SELECT_ROLE_NAME = "SELECT role_name " +
            "FROM role " +
            "WHERE role_id = ?";

    public UserRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(User user){
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_INTO_USER_ACCOUNT)){
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            throw new DatabaseException("Не удалось сохранить пользователя: " + user.getUserName(), e);
        }
    }

    public void saveWithRole(User user){
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_ADMIN_INSERT_INTO_USER_ACCOUNT)){
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, user.getRoleId());
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            throw new DatabaseException("Не удалось сохранить пользователя: " + user.getUserName(), e);
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_USER_FROM_USER_ACCOUNT)){

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                User user = User.builder()
                        .id(resultSet.getInt("user_id"))
                        .userName(resultSet.getString("username"))
                        .email(resultSet.getString("email"))
                        .regDate(resultSet.getString("reg_date"))
                        .roleId(resultSet.getInt("role_id"))
                        .build();
                users.add(user);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Не удалось найти всех пользователей", e);
        }
        return users;
    }

    @Override
    public User findByEmail(String email) {

        User user = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_USER_FROM_USER_ACCOUNT_BY_EMAIL)){

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                user = User.builder()
                        .id(resultSet.getInt("user_id"))
                        .userName(resultSet.getString("username"))
                        .email(resultSet.getString("email"))
                        .password(resultSet.getString("password"))
                        .roleId(resultSet.getInt("role_id"))
                        .regDate(resultSet.getString("reg_date"))
                        .build();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Не удалось найти пользователя по электронной почте: " + email, e);
        }

        return user;
    }

    @Override
    public User findByUsername(String username) {

        User user = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_USER_FROM_USER_ACCOUNT_BY_USERNAME)){

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                user = User.builder()
                        .userName(resultSet.getString("username"))
                        .email(resultSet.getString("email"))
                        .password(resultSet.getString("password"))
                        .roleId(resultSet.getInt("role_id"))
                        .build();
            } else {
                throw new EntityNotFoundException("User", username);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Не удалось найти пользователя по имени: " + username, e);
        }

        return user;
    }

    @Override
    public String getUserRole(Integer roleId) {
        if (roleId == null){
            return null;
        }

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ROLE_NAME)){

            statement.setInt(1, roleId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                return resultSet.getString("role_name");
            }

        } catch (SQLException e){
            throw new DatabaseException("Не удалось найти роль с идентификатором: " + roleId, e);
        }

        return null;
    }
}
