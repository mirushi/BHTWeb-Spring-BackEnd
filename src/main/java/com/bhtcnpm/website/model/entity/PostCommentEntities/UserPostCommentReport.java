package com.bhtcnpm.website.model.entity.PostCommentEntities;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPostCommentReport {
    @EmbeddedId
    private UserPostCommentReportId userPostCommentReportId;

    @OneToMany(
            mappedBy = "postCommentReportReasonId.userPostCommentReport",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Fetch(FetchMode.JOIN)
    private List<PostCommentReportReason> reasons;

    private String feedback;
}
