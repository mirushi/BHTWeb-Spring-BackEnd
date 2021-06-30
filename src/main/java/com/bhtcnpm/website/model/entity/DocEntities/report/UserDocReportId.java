package com.bhtcnpm.website.model.entity.DocEntities.report;

import com.bhtcnpm.website.model.entity.UserWebsite;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDocReportId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserWebsite user;

    @ManyToOne
    @JoinColumn(name = "doc_report_id")
    private DocReport docReport;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDocReportId)) return false;
        UserDocReportId that = (UserDocReportId) o;
        return Objects.equals(getUser(), that.getUser())
                && Objects.equals(getDocReport(), that.getDocReport());
    }

    @Override
    public int hashCode() {return Objects.hash(getUser(), getDocReport());}
}
