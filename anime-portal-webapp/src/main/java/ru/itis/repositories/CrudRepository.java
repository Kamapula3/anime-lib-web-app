package ru.itis.repositories;

import java.sql.SQLException;
import java.util.List;

public interface CrudRepository <T>{

    void save(T entity);

    List<T> findAll();
}
