package com.test.librarymanagement.exception.handler;

import com.test.librarymanagement.exception.LibraryManagementException;
import com.test.librarymanagement.exception.LibraryManagementValidationException;
import com.test.librarymanagement.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.test.librarymanagement.constant.ExceptionConstant.*;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(final IllegalArgumentException exception) {
        log.error("IllegalArgumentException exception occurred: {}", exception.getMessage());

        return new ResponseEntity<>(new ErrorResponse(String.format("%s, %s", ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE, exception.getMessage()))
                , HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("HttpRequestMethodNotSupported exception occurred: {}", ex.getMessage());

        return new ResponseEntity<>(new ErrorResponse(String.format("%s", ex.getMessage()))
                , HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("HttpMediaTypeNotAcceptable exception occurred: {}", ex.getMessage());

        return new ResponseEntity<>(new ErrorResponse(String.format("%s", ex.getMessage()))
                , HttpStatus.NOT_ACCEPTABLE);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("MissingPathVariable exception occurred: {}", ex.getMessage());

        return new ResponseEntity<>(new ErrorResponse(String.format("%s", ex.getMessage()))
                , status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("MissingServletRequestParameter exception occurred: {}", ex.getMessage());

        return new ResponseEntity<>(new ErrorResponse(String.format("%s", ex.getMessage()))
                , status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
           var fieldName = ((FieldError) error).getField();
           var errorMessage = error.getDefaultMessage();
           errors.put(fieldName, errorMessage);
        });
        String errorsString = errors.toString();

        log.error("MethodArgumentNotValidException exception occurred: {}", errorsString);
        return new ResponseEntity<>(new ErrorResponse(String.format("%s", errorsString))
                , status);
    }


    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(final IllegalStateException exception) {
        log.error("IllegalStateException exception occurred: {}", exception.getMessage());

        return new ResponseEntity<>(new ErrorResponse(String.format("%s, %s", ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE, exception.getMessage()))
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(final NoSuchElementException exception) {
        log.error("NoSuchElementException exception occurred: {}", exception.getMessage());

        return new ResponseEntity<>(new ErrorResponse(String.format("%s, %s", NO_SUCH_ELEMENT_EXCEPTION_MESSAGE, exception.getMessage()))
                , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LibraryManagementException.class)
    public ResponseEntity<ErrorResponse> handleAMPException(LibraryManagementException exception) {
        log.error("AMPException exception occurred", exception);

        return new ResponseEntity<>(new ErrorResponse(String.format("%s, %s", LIBRARY_MANAGEMENT_EXCEPTION_MESSAGE, exception.getMessage()))
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LibraryManagementValidationException.class)
    public ResponseEntity<ErrorResponse> handleHigherServiceValidationException(final LibraryManagementValidationException e) {
        log.error("AMPValidationException occurred: {}", e.getErrors().toString());
        return new ResponseEntity<>(new ErrorResponse(e.getErrors().stream()
                .map(error -> String.format("%s: %s.", LIBRARY_MANAGEMENT_VALIDATION_EXCEPTION_MESSAGE, error)).toList().toString()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        log.error("DataIntegrityViolationException exception occurred: {}", exception.getMessage());
        String message = NestedExceptionUtils.getMostSpecificCause(exception).getMessage();
        return new ResponseEntity<>(new ErrorResponse(String.format("%s", message))
                , HttpStatus.CONFLICT);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        log.error("ConstraintViolationException exception occurred: {}", exception.getMessage());
        return new ResponseEntity<>(new ErrorResponse(String.format("%s, %s", ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE, exception.getMessage()))
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorResponse> handleMultipartException(MultipartException exception) {
        log.error("MultipartException exception occurred: {}", exception.getMessage());
        return new ResponseEntity<>(new ErrorResponse(String.format("%s, %s", ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE, exception.getMessage()))
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(final AuthenticationException exception) {
        log.error("AuthenticationException exception occurred: {}", exception.getMessage());

        return new ResponseEntity<>(new ErrorResponse(String.format("%s, %s", AUTHENTICATION_EXCEPTION_MESSAGE, exception.getMessage()))
                , HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(final AccessDeniedException exception) {
        log.error("AccessDeniedException exception occurred: {}", exception.getMessage());

        return new ResponseEntity<>(new ErrorResponse(String.format("%s, %s", exception.getMessage(), PERMISSION_DENIED))
                , HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleAllOtherExceptions(Throwable exception) {
        log.error("Throwable exception occurred", exception);

        return new ResponseEntity<>(new ErrorResponse(THROWABLE_MESSAGE)
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
