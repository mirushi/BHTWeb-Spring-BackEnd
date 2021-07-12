package com.bhtcnpm.website.model.entity.PostEntities;

import com.bhtcnpm.website.model.entity.UserWebsite;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPostReportId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserWebsite user;

    @ManyToOne
    @JoinColumn(name = "post_report_id")
    private PostReport postReport;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPostReportId)) return false;
        UserPostReportId that = (UserPostReportId) o;
        return Objects.equals(getUser(), that.getUser())
                && Objects.equals(getPostReport(), that.getPostReport());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getPostReport());
    }
}
