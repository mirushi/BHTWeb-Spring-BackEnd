package com.bhtcnpm.website.model.entity.PostEntities;

import com.bhtcnpm.website.model.entity.ReportReason;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class PostReportReasonId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "post_report_id")
    private PostReport postReport;

    @ManyToOne
    @JoinColumn(name = "reason_id")
    private ReportReason reportReason;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof PostReportReasonId)) return false;
        PostReportReasonId that = (PostReportReasonId) o;
        return Objects.equals(getPostReport(), that.getPostReport()) &&
                Objects.equals(getReportReason(), that.getReportReason());
    }

}
