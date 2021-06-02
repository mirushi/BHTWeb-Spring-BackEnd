package com.bhtcnpm.website.model.dto.UserPostReport;

import lombok.Data;

import java.util.List;

@Data
public class UserPostReportRequestDTO {
    private List<Long> reasonIds;
    private String feedback;
}
