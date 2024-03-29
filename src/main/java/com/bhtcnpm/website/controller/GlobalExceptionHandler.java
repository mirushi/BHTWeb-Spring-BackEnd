package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.constant.business.ApiErrorReason;
import com.bhtcnpm.website.model.dto.ApiError;
import com.bhtcnpm.website.model.dto.ApiErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String MALFORMED_REQUEST = "Your request is malformed. Please check again.";

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public final ApiError handleIllegalRequest(IllegalArgumentException illegalArgumentException) {
        String message = MALFORMED_REQUEST;
        ApiError apiError = createApiError(HttpStatus.BAD_REQUEST, new ApiErrorDetails(
                null,
                null,
                null,
                message,
                ApiErrorReason.ILLEGAL_REQUEST
        ));

        return apiError;
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public final ApiError handleConstraintViolation (ConstraintViolationException constraintViolationException) {
        String message = MALFORMED_REQUEST;
        ApiError apiError = createApiError(HttpStatus.BAD_REQUEST, new ApiErrorDetails(
                null,
                null,
                null,
                message,
                ApiErrorReason.ILLEGAL_REQUEST
        ));

        return apiError;
    }

    @ExceptionHandler(value = {ObjectOptimisticLockingFailureException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public final ApiError handleVersioningError (ObjectOptimisticLockingFailureException objectOptimisticLockingFailureException) {
        String message = "Please refresh the webpage for new version of this data.";
        ApiError apiError = createApiError(HttpStatus.BAD_REQUEST, new ApiErrorDetails(
                null,
                null,
                null,
                message,
                ApiErrorReason.ILLEGAL_REQUEST
        ));

        return apiError;
    }

    private ApiError createApiError (HttpStatus status, ApiErrorDetails... apiErrorDetails) {
        return new ApiError(status.value(), Arrays.asList(apiErrorDetails));
    }

}
