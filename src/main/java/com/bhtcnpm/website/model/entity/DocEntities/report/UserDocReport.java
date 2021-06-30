package com.bhtcnpm.website.model.entity.DocEntities.report;

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
public class UserDocReport {
    @EmbeddedId
    private UserDocReportId userDocReportId;

    @OneToMany(
            mappedBy = "docReportReasonId.userDocReport",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Fetch(FetchMode.JOIN)
    private List<DocReportReason> reasons;

    private String feedback;
}
