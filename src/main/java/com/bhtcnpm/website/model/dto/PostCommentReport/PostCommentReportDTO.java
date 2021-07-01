package com.bhtcnpm.website.model.dto.PostCommentReport;

import com.bhtcnpm.website.model.dto.ReportReason.ReportReasonDTO;
import com.bhtcnpm.website.model.dto.UserWebsite.UserSummaryDTO;
import com.bhtcnpm.website.model.entity.enumeration.PostCommentReportAction.PostCommentReportActionType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class PostCommentReportDTO {
    private Long id;
    private Long commentID;
    private UserSummaryDTO author;
    private Long postID;
    private String postTitle;
    private String content;
    private List<UserSummaryDTO> reporters;
    private Set<ReportReasonDTO> reportReasons;
    private List<String> feedbacks;
    private LocalDateTime reportTime;
    private LocalDateTime resolvedTime;
    private String resolvedNote;
    private UserSummaryDTO resolvedBy;
    private PostCommentReportActionType actionTaken;
}
