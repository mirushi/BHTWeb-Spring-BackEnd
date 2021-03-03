package com.bhtcnpm.website.model.dto.UserPostReport;

import lombok.Data;

import java.util.List;

@Data
public class UserPostReportListDTO {
    private List<UserPostReportDTO> userPostReportDTOs;
    private Long totalElements;
    private Integer totalPages;
}
