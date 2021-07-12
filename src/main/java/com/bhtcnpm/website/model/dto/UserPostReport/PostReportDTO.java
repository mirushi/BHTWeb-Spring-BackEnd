package com.bhtcnpm.website.model.dto.UserPostReport;

import com.bhtcnpm.website.model.dto.ReportReason.ReportReasonDTO;
import com.bhtcnpm.website.model.dto.UserWebsite.UserSummaryDTO;
import com.bhtcnpm.website.model.entity.enumeration.PostReportAction.PostReportActionType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class PostReportDTO {
    private Long id;
    private Long postID;
    private String postImageURL;
    private String title;
    private String content;
    private UserSummaryDTO author;
    private List<UserSummaryDTO> reporters;
    private Set<ReportReasonDTO> reportReasons;
    private List<String> feedbacks;
    private LocalDateTime reportTime;
    private LocalDateTime resolvedTime;
    private String resolvedNote;
    private UserSummaryDTO resolvedBy;
    private PostReportActionType actionTaken;
}
