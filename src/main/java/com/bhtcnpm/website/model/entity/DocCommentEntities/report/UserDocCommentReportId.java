package com.bhtcnpm.website.model.entity.DocCommentEntities.report;

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
public class UserDocCommentReportId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserWebsite user;

    @ManyToOne
    @JoinColumn(name = "doc_comment_report_id")
    private DocCommentReport docCommentReport;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDocCommentReportId)) return false;
        UserDocCommentReportId that = (UserDocCommentReportId) o;
        return Objects.equals(getUser(), that.getUser())
                && Objects.equals(getDocCommentReport(), that.getDocCommentReport());
    }

    @Override
    public int hashCode() {return Objects.hash(getUser(), getDocCommentReport());}
}
