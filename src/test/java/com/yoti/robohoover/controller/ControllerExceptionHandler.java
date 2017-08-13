package com.yoti.robohoover.controller;

import com.yoti.robohoover.exception.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class ControllerExceptionHandler {
    private static final String INVALID_REQUEST_PARAMS = "Invalid end point parameters";

    @ExceptionHandler(InvalidInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleInvalidInputException(InvalidInputException error) {
        return createBadRequestFrom(error);
    }

    private ErrorResponse createBadRequestFrom(Exception error) {
        return new ErrorResponse(BAD_REQUEST.value(), INVALID_REQUEST_PARAMS, error.getMessage());
    }
}