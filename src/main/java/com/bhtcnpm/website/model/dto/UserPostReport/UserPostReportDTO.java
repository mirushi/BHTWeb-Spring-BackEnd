package com.bhtcnpm.website.model.dto.UserPostReport;

import com.bhtcnpm.website.model.dto.UserWebsite.UserSummaryDTO;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.PostReportAction.PostReportActionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserPostReportDTO {
    private Long id;
    private Long postId;
    private UserSummaryDTO reporter;
    private String reason;
    private LocalDateTime reportTime;
    private LocalDateTime resolvedTime;
    private String resolvedNote;
    private PostReportActionType actionTaken;
}
