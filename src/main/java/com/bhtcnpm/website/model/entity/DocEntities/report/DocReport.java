package com.bhtcnpm.website.model.entity.DocEntities.report;

import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.DocReport.DocReportActionType;
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
        name = "docReport.all",
        attributeNodes = {
                @NamedAttributeNode(value = "userDocReports")
        }
)
public class DocReport {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "doc_report_sequence"
    )
    @SequenceGenerator(
            name = "doc_report_sequence",
            sequenceName = "doc_report_sequence"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "doc_id",
            updatable = false
    )
    private Doc doc;

    @OneToMany(
            mappedBy = "userDocReportId.docReport",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<UserDocReport> userDocReports;

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
    private DocReportActionType actionTaken;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof DocReport)) return false;
        DocReport that = (DocReport) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {return getClass().hashCode();}
}
