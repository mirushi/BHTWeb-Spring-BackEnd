package com.bhtcnpm.website.model.entity.DocEntities.report;

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
public class DocReportReasonId implements Serializable {
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            @JoinColumn(name = "doc_report_id", referencedColumnName = "doc_report_id")
    })
    private UserDocReport userDocReport;

    @ManyToOne
    @JoinColumn(name = "report_reason_id")
    private ReportReason reportReason;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof DocReportReasonId)) return false;
        DocReportReasonId that = (DocReportReasonId) o;
        return Objects.equals(getUserDocReport(), that.getUserDocReport())
                && Objects.equals(getReportReason(), that.getReportReason());
    }

    @Override
    public int hashCode() {return Objects.hash(getUserDocReport(), getReportReason());}
}
