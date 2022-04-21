package ru.chirkovprojects.insidetest.service.exception;

public class EntityDontExistException extends RuntimeException {

    public EntityDontExistException(String message) {
        super(message);
    }

}
