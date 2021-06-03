package com.bhtcnpm.website.model.entity.PostEntities;

import com.bhtcnpm.website.model.entity.ReportReason.ReportReason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostReportReasonId implements Serializable {
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            @JoinColumn(name = "post_report_id", referencedColumnName = "post_report_id")
    })
    private UserPostReport userPostReport;

    @ManyToOne
    @JoinColumn(name = "report_reason_id")
    private ReportReason reportReason;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof PostReportReasonId)) return false;
        PostReportReasonId that = (PostReportReasonId) o;
        return Objects.equals(getUserPostReport(), that.getUserPostReport()) &&
                Objects.equals(getReportReason(), that.getReportReason());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserPostReport(), getReportReason());
    }
}
