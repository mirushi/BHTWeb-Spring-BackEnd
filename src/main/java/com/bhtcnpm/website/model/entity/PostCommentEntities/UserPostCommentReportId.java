package com.bhtcnpm.website.model.entity.PostCommentEntities;

import com.bhtcnpm.website.model.entity.PostEntities.UserPostReportId;
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
public class UserPostCommentReportId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserWebsite user;

    @ManyToOne
    @JoinColumn(name = "post_comment_report_id")
    private PostCommentReport postCommentReport;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if ( !(o instanceof UserPostCommentReportId)) return false;
        UserPostCommentReportId that = (UserPostCommentReportId) o;
        return Objects.equals(getUser(), that.getUser())
                && Objects.equals(getPostCommentReport(), that.getPostCommentReport());
    }

    @Override
    public int hashCode() {return Objects.hash(getUser(), getPostCommentReport());}
}
