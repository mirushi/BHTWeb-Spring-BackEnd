package com.bhtcnpm.website.model.entity.PostCommentEntities;

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
public class PostCommentReportReasonId implements Serializable {
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            @JoinColumn(name = "post_comment_report_id", referencedColumnName = "post_comment_report_id")
    })
    private UserPostCommentReport userPostCommentReport;

    @ManyToOne
    @JoinColumn(name = "report_reason_id")
    private ReportReason reportReason;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof PostCommentReportReasonId)) return false;
        PostCommentReportReasonId that = (PostCommentReportReasonId) o;
        return Objects.equals(getUserPostCommentReport(), that.getUserPostCommentReport())
                && Objects.equals(getReportReason(), that.getReportReason());
    }

    @Override
    public int hashCode() {return Objects.hash(getUserPostCommentReport(), getReportReason());}
}
