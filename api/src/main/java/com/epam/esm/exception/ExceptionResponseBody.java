package com.epam.esm.exception;

import java.time.Instant;

public class ExceptionResponseBody {
    private final String errorMessage;
    private final String errorCode;
    private final Instant errorTime;

    public ExceptionResponseBody(String errorMessage, String errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        errorTime = Instant.now();
    }

    public ExceptionResponseBody(String errorMessage, String errorCode, Object invalidValue) {
        this.errorMessage = errorMessage + invalidValue;
        this.errorCode = errorCode;
        errorTime = Instant.now();
    }

    public String getErrorMessage(){
        return errorMessage;
    }

    public String getErrorCode(){
        return errorCode;
    }

    public Instant getErrorTime(){
        return errorTime;
    }
}
