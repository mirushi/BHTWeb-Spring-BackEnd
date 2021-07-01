package com.bhtcnpm.website.model.entity.ReportReason;

import com.bhtcnpm.website.model.entity.PostEntities.PostReport;
import com.bhtcnpm.website.model.entity.UserWebsite;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class ReportReasonFeedbackId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "user")
    private UserWebsite user;

    @ManyToOne
    @JoinColumn(name = "post_report")
    private PostReport postReport;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportReasonFeedbackId)) return false;
        ReportReasonFeedbackId that = (ReportReasonFeedbackId) o;
        return Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getPostReport(), that.getPostReport());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getPostReport());
    }

}
