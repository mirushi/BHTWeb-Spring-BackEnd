package com.bhtcnpm.website.model.entity.DocCommentEntities.report;

import com.bhtcnpm.website.model.entity.DocCommentEntities.DocComment;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.DocCommentReport.DocCommentReportActionType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NamedEntityGraph(
        name = "docCommentReport.all",
        attributeNodes = {
                @NamedAttributeNode(value = "userDocCommentReports")
        }
)
public class DocCommentReport {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "doc_comment_report_sequence"
    )
    @SequenceGenerator(
            name = "doc_comment_report_sequence",
            sequenceName = "doc_comment_report_sequence"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "doc_comment_id",
            updatable = false
    )
    private DocComment docComment;

    @OneToMany(
            mappedBy = "userDocCommentReportId.docCommentReport",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<UserDocCommentReport> userDocCommentReports;

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
    private DocCommentReportActionType actionTaken;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof DocCommentReport)) return false;
        DocCommentReport that = (DocCommentReport) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {return getClass().hashCode();}
}
