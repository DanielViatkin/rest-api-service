package com.epam.esm.service.exception;

public class NoAuthoritiesException extends RuntimeException{
    private String httpStatus;
    public NoAuthoritiesException() {
        super();
    }
    public NoAuthoritiesException(Exception e) {
        super(e);
    }
    public NoAuthoritiesException(String message, String httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }
    public NoAuthoritiesException(String message, Exception e){
        super(message, e);
    }

    public String getHttpStatus(){
        return httpStatus;
    }
}
