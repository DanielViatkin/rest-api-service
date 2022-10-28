package com.epam.esm.service.exception;

public class InvalidUserPassword extends RuntimeException{
    public InvalidUserPassword() {
        super();
    }
    public InvalidUserPassword(Exception e) {
        super(e);
    }
    public InvalidUserPassword(String message){
        super(message);
    }
    public InvalidUserPassword(String message, Exception e){
        super(message, e);
    }
}
