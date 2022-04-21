package ru.chirkovprojects.insidetest.service.exception;

public class EntityAlreadyExistException extends RuntimeException {

    public EntityAlreadyExistException(String message) {
        super(message);
    }

}
