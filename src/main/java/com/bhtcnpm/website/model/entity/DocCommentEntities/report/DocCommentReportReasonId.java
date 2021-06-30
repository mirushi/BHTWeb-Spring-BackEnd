package com.bhtcnpm.website.model.entity.DocCommentEntities.report;

import com.bhtcnpm.website.model.entity.ReportReason.ReportReason;
import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocCommentReportReasonId implements Serializable {
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            @JoinColumn(name = "doc_comment_report_id", referencedColumnName = "doc_comment_report_id")
    })
    private UserDocCommentReport userDocCommentReport;

    @ManyToOne
    @JoinColumn(name = "report_reason_id")
    private ReportReason reportReason;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof DocCommentReportReasonId)) return false;
        DocCommentReportReasonId that = (DocCommentReportReasonId) o;
        return Objects.equals(getUserDocCommentReport(), that.getUserDocCommentReport())
                && Objects.equals(getReportReason(), that.getReportReason());
    }

    @Override
    public int hashCode() {return Objects.hash(getUserDocCommentReport(), getReportReason());}
}
