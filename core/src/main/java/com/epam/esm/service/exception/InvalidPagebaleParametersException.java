package com.epam.esm.service.exception;

public class InvalidPagebaleParametersException extends RuntimeException{
    public InvalidPagebaleParametersException() {
        super();
    }
    public InvalidPagebaleParametersException(Exception e, Object invalidValue) {
        super(e);
    }
    public InvalidPagebaleParametersException(String message, Object invalidValue){
        super(message);
    }
    public InvalidPagebaleParametersException(String message, Exception e, Object invalidValue){
        super(message, e);
    }
}
