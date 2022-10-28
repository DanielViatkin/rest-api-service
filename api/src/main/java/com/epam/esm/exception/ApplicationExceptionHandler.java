package com.epam.esm.exception;

import com.epam.esm.service.exception.*;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
    private static final List<String> AVAILABLE_LOCALES = Arrays.asList("en_US", "ru_RU");
    private static final Locale DEFAULT_LOCALE = new Locale("en", "US");

    private final ResourceBundleMessageSource bundleMessageSource;

    @Autowired
    public ApplicationExceptionHandler(ResourceBundleMessageSource bundleMessageSource) {
        this.bundleMessageSource = bundleMessageSource;
    }

    private ExceptionResponseBody buildExceptionResponseBody(String errorMessage, String errorCode){
        return new ExceptionResponseBody(errorMessage, errorCode);
    }

    private ExceptionResponseBody buildExceptionResponseBody(String errorMessage, String errorCode, Object invalidValue){
        return new ExceptionResponseBody(errorMessage, errorCode, invalidValue);
    }

    private String localize(String message, Locale locale){
        if (!AVAILABLE_LOCALES.contains(locale.toString())){
            locale = DEFAULT_LOCALE;
        }
        return bundleMessageSource.getMessage(message,null,locale);
    }

    @ExceptionHandler(NoAuthoritiesException.class)
    public ResponseEntity<ExceptionResponseBody> noAuthoritiesExceptionHandler(NoAuthoritiesException e, Locale locale){
        return  ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(buildExceptionResponseBody(localize(e.getMessage(),locale), e.getHttpStatus()));
    }

    @ExceptionHandler(InvalidCertificateException.class)
    public ResponseEntity<ExceptionResponseBody> invalidCertificationHandler(InvalidCertificateException e, Locale locale){
        return  ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(buildExceptionResponseBody(localize(e.getMessage(),locale), "42201", e.getInvalidValue()));
    }

    @ExceptionHandler(InvalidTagException.class)
    public ResponseEntity<ExceptionResponseBody> invalidTagHandler(InvalidTagException e, Locale locale){
        return  ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(buildExceptionResponseBody(localize(e.getMessage(),locale), "42202", e.getInvalidValue()));
    }

    @ExceptionHandler(InvalidUserPassword.class)
    public ResponseEntity<ExceptionResponseBody> invalidUserPasswordHandler(InvalidUserPassword e, Locale locale){
        return  ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(buildExceptionResponseBody(localize(e.getMessage(),locale), "42210"));
    }

    @ExceptionHandler(NotFoundEntityException.class)
    public ResponseEntity<ExceptionResponseBody> notFoundHandler(NotFoundEntityException e, Locale locale){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildExceptionResponseBody(localize(e.getMessage(), locale), "40401", e.getInvalidValue()));
    }

    @ExceptionHandler(EntityAlreadyExistException.class)
    public ResponseEntity<ExceptionResponseBody> entityAlreadyExistHandler(EntityAlreadyExistException e, Locale locale){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(buildExceptionResponseBody(localize(e.getMessage(), locale), "40901"));
    }

    @ExceptionHandler(InvalidParamException.class)
    public ResponseEntity<ExceptionResponseBody> invalidParamHandler(InvalidParamException e, Locale locale){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildExceptionResponseBody(localize(e.getMessage(), locale), "40030"));
    }

    @ExceptionHandler(InvalidPagebaleParametersException.class)
    public ResponseEntity<ExceptionResponseBody> invalidParamHandler(InvalidPagebaleParametersException e, Locale locale){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildExceptionResponseBody(localize(e.getMessage(), locale), "40040"));
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<ExceptionResponseBody> jwtAuthenticationHandler(JwtAuthenticationException e, Locale locale){
        System.out.println("jwtAuthenticationHandler");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildExceptionResponseBody(localize(e.getMessage(), locale), "40101"));
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponseBody> noHandlerFoundExceptionHandler(MethodArgumentTypeMismatchException e){
        String errorMessage = "invalid.request";
        return new ResponseEntity<>(buildExceptionResponseBody(localize(errorMessage, DEFAULT_LOCALE),"40010"), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessage = "invalid.http.message";
        System.out.println("handleHttpMessageNotReadable");
        return new ResponseEntity<>(buildExceptionResponseBody(localize(errorMessage, DEFAULT_LOCALE),"40020"), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessage = "not.found.resource";
        System.out.println("handleNoHandlerFoundException");
        return new ResponseEntity<>(buildExceptionResponseBody(localize(errorMessage, DEFAULT_LOCALE),"40401"), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        System.out.println("handleHttpMediaTypeNotAcceptable");
        return super.handleHttpMediaTypeNotAcceptable(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        System.out.println("handleHttpRequestMethodNotSupported");
        String errorMessage = "not.found.resource";
        return new ResponseEntity<>(buildExceptionResponseBody(localize(errorMessage, DEFAULT_LOCALE),"40501"), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        System.out.println("handleHttpMediaTypeNotSupported");
        return super.handleHttpMediaTypeNotSupported(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        System.out.println("handleMissingPathVariable");
        return super.handleMissingPathVariable(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        System.out.println("handleMissingServletRequestParameter");
        return super.handleMissingServletRequestParameter(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        System.out.println("handleServletRequestBindingException");
        return super.handleServletRequestBindingException(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        System.out.println("handleConversionNotSupported");
        return super.handleConversionNotSupported(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        System.out.println("handleTypeMismatch");
        return super.handleTypeMismatch(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        System.out.println("handleHttpMessageNotWritable");
        return super.handleHttpMessageNotWritable(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        System.out.println("handleMethodArgumentNotValid");
        String errorMessage = "invalid.request.args";
        return new ResponseEntity<>(buildExceptionResponseBody(localize(errorMessage, DEFAULT_LOCALE),"40010"), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        System.out.println("handleMissingServletRequestPart");
        return super.handleMissingServletRequestPart(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        System.out.println("handleBindException");
        return super.handleBindException(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        System.out.println("handleAsyncRequestTimeoutException");
        return super.handleAsyncRequestTimeoutException(ex, headers, status, webRequest);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        System.out.println("handleExceptionInternal");
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }
}
