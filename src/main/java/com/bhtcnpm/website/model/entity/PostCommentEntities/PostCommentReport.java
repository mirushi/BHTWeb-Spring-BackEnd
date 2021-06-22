package com.bhtcnpm.website.model.entity.PostCommentEntities;

import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.PostCommentReportAction.PostCommentReportActionType;
import com.bhtcnpm.website.model.validator.entity.PostCommentReport.ValidPCREntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidPCREntity
@NamedEntityGraph(
        name = "postCommentReport.all",
        attributeNodes = {
                @NamedAttributeNode(value = "userPostCommentReports")
        }
)
public class PostCommentReport {
    @Id
    @GeneratedValue (
            strategy = GenerationType.SEQUENCE,
            generator = "post_comment_report_sequence"
    )
    @SequenceGenerator(
            name = "post_comment_report_sequence",
            sequenceName = "post_comment_report_sequence"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "post_comment_id",
            updatable = false
    )
    private PostComment postComment;

    @OneToMany(
            mappedBy = "userPostCommentReportId.postCommentReport",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<UserPostCommentReport> userPostCommentReports;

    @Column(name = "report_time")
    private LocalDateTime reportTime;

    @Column(name = "resolved_time")
    private LocalDateTime resolvedTime;

    @JoinColumn(name = "resolved_by")
    @ManyToOne
    private UserWebsite resolvedBy;

    @Column(name = "resolved_note")
    private String resolvedNote;

    @Enumerated
    @Column(columnDefinition = "smallint")
    private PostCommentReportActionType actionTaken;
}
