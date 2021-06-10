package com.bhtcnpm.website.model.entity.PostEntities;

import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.PostReportAction.PostReportActionType;
import com.bhtcnpm.website.model.validator.entity.UserPostReport.ValidUPREntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "post_report")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidUPREntity
@NamedEntityGraph(
        name = "postReport.all",
        attributeNodes = {
                @NamedAttributeNode(value = "userPostReports")
        }
)
public class PostReport {
    @Id
    @GeneratedValue (
            strategy = GenerationType.SEQUENCE,
            generator = "post_report_sequence"
    )
    @SequenceGenerator(
            name = "post_report_sequence",
            sequenceName = "post_report_sequence"
    )
    private Long id;

    @OneToOne
    @JoinColumn(name = "post_id",
            unique = true,
            updatable = false)
    private Post post;

    @OneToMany (
            mappedBy = "userPostReportId.postReport",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<UserPostReport> userPostReports;

    @Column(name = "report_time")
    @UpdateTimestamp
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
