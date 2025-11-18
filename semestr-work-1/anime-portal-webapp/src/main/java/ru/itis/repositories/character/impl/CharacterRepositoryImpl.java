package ru.itis.repositories.character.impl;

import ru.itis.exceptions.DatabaseException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ru.itis.models.CharacterModel;
import ru.itis.repositories.character.CharacterRepository;

public class CharacterRepositoryImpl implements CharacterRepository {

    private static String SQL_SELECT_CHARACTERS_BY_ANIME_ID = "SELECT ch.character_id, ch.name, ch.surname, ch.description, ch.anime_id " +
            "FROM character ch " +
            "JOIN anime a ON a.anime_id = ch.anime_id " +
            "WHERE a.anime_id = ?";

    private static String SQL_SELECT_ALL_CHARACTERS = "SELECT character_id, name, surname, description, anime_id " +
            "FROM character";

    private static String SQL_INSERT_INTO_CHARACTER = "INSERT INTO character(name, surname, description, anime_id) VALUES(?, ?, ?, ?)";


    private DataSource dataSource;

    public CharacterRepositoryImpl(DataSource dataSource) { this.dataSource = dataSource; }

    @Override
    public List<CharacterModel> findAllByAnimeId(Integer animeId){

        List<CharacterModel> characters = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_CHARACTERS_BY_ANIME_ID)){

            statement.setInt(1, animeId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                CharacterModel character = CharacterModel.builder()
                        .id(resultSet.getInt("character_id"))
                        .name(resultSet.getString("name"))
                        .surname(resultSet.getString("surname"))
                        .description(resultSet.getString("description"))
                        .anime_id(animeId)
                        .build();
                characters.add(character);
            }
        } catch (SQLException e){
            throw new DatabaseException("Не удалось найти героев аниме с идентификатором: " + animeId, e);
        }

        return characters;
    }

    @Override
    public void save(CharacterModel characterModel) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT_INTO_CHARACTER)){
            statement.setString(1, characterModel.getName());
            statement.setString(2, characterModel.getSurname());
            statement.setString(3, characterModel.getDescription());
            statement.setInt(4, characterModel.getAnime_id());
            statement.executeUpdate();
        } catch (SQLException e){
            throw new DatabaseException("Не удалось сохранить персонажа: " + characterModel.getName(), e);
        }
    }

    @Override
    public List<CharacterModel> findAll() {
        List<CharacterModel> characterModelList = new ArrayList<>();

        try (Connection connection =  dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL_CHARACTERS)){

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                CharacterModel characterModel = CharacterModel.builder()
                        .id(resultSet.getInt("character_id"))
                        .name(resultSet.getString("name"))
                        .surname(resultSet.getString("surname"))
                        .description(resultSet.getString("description"))
                        .anime_id(resultSet.getInt("anime_id"))
                        .build();

                characterModelList.add(characterModel);
            }
        } catch (SQLException e){
            throw new DatabaseException("Не удалось найти всеx персонажей", e);
        }

        return characterModelList;
    }
}
