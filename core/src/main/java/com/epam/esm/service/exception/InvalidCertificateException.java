package com.epam.esm.service.exception;

public class InvalidCertificateException extends RuntimeException{
    private Object invalidValue;
    public InvalidCertificateException() {
        super();
    }
    public InvalidCertificateException(Exception e, Object invalidValue) {
        super(e);
        this.invalidValue = invalidValue;
    }
    public InvalidCertificateException(String message, Object invalidValue){
        super(message);
        this.invalidValue = invalidValue;
    }
    public InvalidCertificateException(String message, Exception e, Object invalidValue){
        super(message, e);
        this.invalidValue = invalidValue;
    }
    public Object getInvalidValue(){
        return invalidValue;
    }
}
