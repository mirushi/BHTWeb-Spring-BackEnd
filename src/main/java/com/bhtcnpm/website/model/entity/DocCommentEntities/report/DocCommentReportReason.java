package com.bhtcnpm.website.model.entity.DocCommentEntities.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "doc_comment_report_reason")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocCommentReportReason {
    @EmbeddedId
    private DocCommentReportReasonId docCommentReportReasonId;
}
