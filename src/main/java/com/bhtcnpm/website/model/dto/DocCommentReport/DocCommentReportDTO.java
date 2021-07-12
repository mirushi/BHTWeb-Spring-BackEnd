package com.bhtcnpm.website.model.dto.DocCommentReport;

import com.bhtcnpm.website.model.dto.Doc.DocFileUploadDTO;
import com.bhtcnpm.website.model.dto.ReportReason.ReportReasonDTO;
import com.bhtcnpm.website.model.dto.Tag.TagDTO;
import com.bhtcnpm.website.model.dto.UserWebsite.UserSummaryDTO;
import com.bhtcnpm.website.model.entity.enumeration.DocReport.DocReportActionType;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Value
public class DocCommentReportDTO {
    Long id;
    Long commentID;
    String content;
    Long docID;
    String docTitle;
    String description;
    String imageURL;
    UserSummaryDTO author;
    LocalDateTime submitDtm;
    Long categoryID;
    String categoryName;
    Long subjectID;
    String subjectName;
    List<DocFileUploadDTO> docFileUploadDTOs;
    Set<TagDTO> tags;
    List<UserSummaryDTO> reporters;
    Set<ReportReasonDTO> reportReasons;
    List<String> feedbacks;
    LocalDateTime reportTime;
    LocalDateTime resolvedTime;
    String resolvedNote;
    UserSummaryDTO resolvedBy;
    DocReportActionType actionTaken;
}
