package com.epam.esm.service.exception;

public class NotFoundEntityException extends RuntimeException{
    private Object invalidValue;

    public NotFoundEntityException() {
        super();
    }

    public NotFoundEntityException(Exception e, Object invalidValue) {
        super(e);
        this.invalidValue = invalidValue;
    }

    public NotFoundEntityException(String message, Object invalidValue){
        super(message);
        this.invalidValue = invalidValue;
    }

    public NotFoundEntityException(String message,Exception e, Object invalidValue){
        super(message, e);
        this.invalidValue = invalidValue;
    }

    public Object getInvalidValue(){
        return invalidValue;
    }
}
