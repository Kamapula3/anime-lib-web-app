package ru.itis.exceptions;

public class EntityNotFoundException extends DatabaseException{
    public EntityNotFoundException(String entityType, Object identifier){
        super(entityType + "c идентификатором '" + identifier + "' не найден");
    }
}
