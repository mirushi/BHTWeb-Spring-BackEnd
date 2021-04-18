package com.bhtcnpm.website.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailTemplate {
    private String subject;
    private String content;
}
