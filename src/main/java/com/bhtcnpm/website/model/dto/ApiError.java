package com.bhtcnpm.website.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ApiError {
    private int code;
    private List<ApiErrorDetails> errors;
}
