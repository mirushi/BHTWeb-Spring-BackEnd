package com.bhtcnpm.website.model.dto.UserPostReport;

import lombok.Data;

import java.util.List;

@Data
public class UserPostReportListDTO {
    private List<PostReportDTO> postReportDTOS;
    private Long totalElements;
    private Integer totalPages;
}
