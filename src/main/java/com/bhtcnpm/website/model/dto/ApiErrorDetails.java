package com.bhtcnpm.website.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiErrorDetails {
    private String domain;
    private String location;
    private String locationType;
    private String message;
    private String reason;
}
