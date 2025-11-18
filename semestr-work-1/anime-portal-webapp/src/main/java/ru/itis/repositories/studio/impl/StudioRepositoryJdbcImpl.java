package ru.itis.repositories.studio.impl;

import ru.itis.exceptions.DatabaseException;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.models.Studio;
import ru.itis.models.User;
import ru.itis.repositories.studio.StudioRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudioRepositoryJdbcImpl implements StudioRepository {

    private DataSource dataSource;

    private static String SQL_INSERT_STUDIO = "INSERT INTO studio(name, country) " +
            "VALUES(?, ?)";

    private static String SQL_SELECT_ALL_STUDIO_FROM_STUDIO = "SELECT studio_id, name, country FROM studio";

    private static String SQL_SELECT_STUDIO_ID = "SELECT studio_id FROM studio " +
            "WHERE name = ?";

    private static String SQL_SELECT_STUDIO = "SELECT s.studio_id, s.name, s.country " +
            "FROM studio s " +
            "JOIN anime a ON a.studio_id = s.studio_id " +
            "WHERE s.studio_id = ? ";

    public StudioRepositoryJdbcImpl(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public void save(Studio studio) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_STUDIO)){

            statement.setString(1, studio.getName());
            statement.setString(2, studio.getCountry());

            statement.executeUpdate();

        } catch (SQLException e){
            throw new DatabaseException("Не удалось сохранить студию", e);
        }
    }

    @Override
    public List<Studio> findAll() {
        List<Studio> studios = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_STUDIO_FROM_STUDIO)){

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Studio studio = Studio.builder()
                        .id(resultSet.getInt("studio_id"))
                        .name(resultSet.getString("name"))
                        .country(resultSet.getString("country"))
                        .build();

                studios.add(studio);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Не удалось найти всех пользователей", e);
        }
        return studios;
    }

    @Override
    public Integer getStudioId(String studioName) {
        Integer studioId = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_STUDIO_ID)){
            preparedStatement.setString(1, studioName.trim());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                studioId =  resultSet.getInt("studio_id");
            }


        } catch (SQLException e) {
            throw new DatabaseException("Не удалось идентификатор студии", e);
        }

        return studioId;
    }

    @Override
    public Studio getStudio(Integer studioId){
        Studio studio = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_STUDIO)){

            statement.setInt(1, studioId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                studio = Studio.builder()
                        .id(resultSet.getInt("studio_id"))
                        .name(resultSet.getString("name"))
                        .country(resultSet.getString("country"))
                        .build();
            }
        } catch (SQLException e){
            throw new EntityNotFoundException("Не удалось найти студию c идентификатором: " + studioId, e);
        }

        return studio;
    }
}
