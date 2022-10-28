package com.epam.esm.service.exception;

public class InvalidUserException extends RuntimeException{
    private Object invalidValue;
    public InvalidUserException() {
        super();
    }
    public InvalidUserException(Exception e, Object invalidValue) {
        super(e);
        this.invalidValue = invalidValue;
    }
    public InvalidUserException(String message, Object invalidValue){
        super(message);
        this.invalidValue = invalidValue;
    }
    public InvalidUserException(String message, Exception e, Object invalidValue){
        super(message, e);
        this.invalidValue = invalidValue;
    }
    public Object getInvalidValue(){
        return invalidValue;
    }
}
