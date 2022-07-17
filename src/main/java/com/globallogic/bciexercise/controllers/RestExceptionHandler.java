package com.globallogic.bciexercise.controllers;

import com.globallogic.bciexercise.dtos.ErrorResponse;
import com.globallogic.bciexercise.errors.ApiError;
import com.globallogic.bciexercise.errors.ApiErrorCodes;
import com.globallogic.bciexercise.errors.DuplicatedUserException;
import com.globallogic.bciexercise.errors.TokenException;
import com.globallogic.bciexercise.errors.UserNotFoundException;
import com.globallogic.bciexercise.errors.ValidationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<Object> handleValidationError(
            ValidationException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ex.getErrors());
        return buildResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicatedUserException.class)
    protected ResponseEntity<Object> handleDuplicatedUserError(
            DuplicatedUserException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.getError().add(ex.getError());
        return buildResponseEntity(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ TokenException.class})
    public ResponseEntity<Object> handleTokenException(TokenException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.getError().add(ex.getError());

        return buildResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ Exception.class})
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        ApiError error = new ApiError(ApiErrorCodes.INTERNAL_SERVER_EXCEPTION.getCode(),
                String.format("Exception %s - cause: %s - message: %s", ex.getClass().toString(), ex.getCause(), ex.getMessage()));
        errorResponse.getError().add(error);

        return buildResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ UserNotFoundException.class})
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.getError().add(ex.getError());

        return buildResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse, HttpStatus status) {
        return new ResponseEntity<>(errorResponse, status);
    }


}
