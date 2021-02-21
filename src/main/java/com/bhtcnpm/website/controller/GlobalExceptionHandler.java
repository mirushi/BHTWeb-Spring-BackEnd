package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public final ApiError handleIllegalRequest(IllegalArgumentException illegalArgumentException) {
        String message = "Your request is malformed. Please check again.";
        ApiError apiError = new ApiError(message, illegalArgumentException.getMessage());

        return apiError;
    }

    @ExceptionHandler(value = {ObjectOptimisticLockingFailureException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public final ApiError handleVersioningError (ObjectOptimisticLockingFailureException objectOptimisticLockingFailureException) {
        String message = "Please refresh the webpage for new version of this data.";
        ApiError apiError = new ApiError(message, objectOptimisticLockingFailureException.getMessage());

        return apiError;
    }

}
