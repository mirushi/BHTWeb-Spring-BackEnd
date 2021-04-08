package com.bhtcnpm.website.model.entity.PostEntities;

import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.PostReportAction.PostReportActionType;
import com.bhtcnpm.website.model.validator.UserPostReport.ValidUPREntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "user_post_report")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidUPREntity
@NamedEntityGraph(
        name = "postreport.reporter-reason",
        attributeNodes = {
                @NamedAttributeNode(value = "reporters"),
                @NamedAttributeNode(value = "reasons")
        }
)
public class PostReport {

    @Id
    @GeneratedValue (
            strategy = GenerationType.SEQUENCE,
            generator = "user_post_report_sequence"
    )
    @SequenceGenerator(
            name = "user_post_report_sequence",
            sequenceName = "user_post_report_sequence"
    )
    private Long id;

    @OneToMany (
            mappedBy = "userPostReportId.user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "reporter_id")
    private Set<UserPostReport> reporters;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(
            mappedBy = "postReportReasonId.userPostReport",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "reason")
    private Set<PostReportReason> reasons;

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
    private PostReportActionType actionTaken;
}
