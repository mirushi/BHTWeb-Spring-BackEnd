package com.bhtcnpm.website.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiError {

    private String message;
    private String debugMessage;

}
