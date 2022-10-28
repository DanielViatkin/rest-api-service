package com.epam.esm.service.exception;

public class InvalidParamException extends RuntimeException{
    private Object invalidValue;
    public InvalidParamException() {
        super();
    }
    public InvalidParamException(String message) {
        super(message);
    }
    public InvalidParamException(Exception e, Object invalidValue) {
        super(e);
        this.invalidValue = invalidValue;
    }
    public InvalidParamException(String message, Object invalidValue){
        super(message);
        this.invalidValue = invalidValue;
    }
    public InvalidParamException(String message, Exception e, Object invalidValue){
        super(message, e);
        this.invalidValue = invalidValue;
    }
    public Object getInvalidValue(){
        return invalidValue;
    }
}
