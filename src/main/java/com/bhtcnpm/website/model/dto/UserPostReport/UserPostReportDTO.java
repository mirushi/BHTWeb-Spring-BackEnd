package com.bhtcnpm.website.model.dto.UserPostReport;

import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.PostReportAction.PostReportActionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserPostReportDTO {
    private Long id;
    private Long postId;
    private Long reporterId;
    private String reason;
    private LocalDateTime reportTime;
    private LocalDateTime resolvedTime;
    private UserWebsite resolvedBy;
    private LocalDateTime resolvedNote;
    private PostReportActionType actionTaken;
}
