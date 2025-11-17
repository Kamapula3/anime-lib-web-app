package ru.itis.exceptions;

public class EntityAlreadyExistsException extends DatabaseException{
    public EntityAlreadyExistsException(String entityType, Object identifier){
        super(entityType + "c идентификатором '" + identifier + "' уже существует");
    }
}
