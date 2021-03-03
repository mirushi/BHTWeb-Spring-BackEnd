package com.bhtcnpm.website.model.entity.PostEntities;

import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.PostReportAction.PostReportActionType;
import com.bhtcnpm.website.model.validator.UserPostReport.ValidUPREntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_post_report")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidUPREntity
public class UserPostReport {

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

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private UserWebsite reporter;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "reason")
    private String reason;

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
