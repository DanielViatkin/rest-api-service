package com.epam.esm.service.exception;

public class InvalidTagException extends RuntimeException{
    private Object invalidValue;
    public InvalidTagException() {
        super();
    }
    public InvalidTagException(Exception e, Object invalidValue) {
        super(e);
        this.invalidValue = invalidValue;
    }
    public InvalidTagException(String message, Object invalidValue){
        super(message);
        this.invalidValue = invalidValue;
    }
    public InvalidTagException(String message, Exception e, Object invalidValue){
        super(message, e);
        this.invalidValue = invalidValue;
    }
    public Object getInvalidValue(){
        return invalidValue;
    }
}
