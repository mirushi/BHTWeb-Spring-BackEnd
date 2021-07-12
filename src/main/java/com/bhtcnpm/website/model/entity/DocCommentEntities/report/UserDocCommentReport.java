package com.bhtcnpm.website.model.entity.DocCommentEntities.report;

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
public class UserDocCommentReport {
    @EmbeddedId
    private UserDocCommentReportId userDocCommentReportId;

    @OneToMany(
            mappedBy = "docCommentReportReasonId.userDocCommentReport",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Fetch(FetchMode.JOIN)
    private List<DocCommentReportReason> reasons;

    private String feedback;
}
