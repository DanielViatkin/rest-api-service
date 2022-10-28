package com.epam.esm.service.exception;

public class EntityAlreadyExistException extends RuntimeException{
    public EntityAlreadyExistException() {
        super();
    }
    public EntityAlreadyExistException(Exception e) {
        super(e);
    }
    public EntityAlreadyExistException(String message){
        super(message);
    }
    public EntityAlreadyExistException(String message, Exception e){
        super(message, e);
    }
}
