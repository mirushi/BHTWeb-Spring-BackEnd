package com.bhtcnpm.website.model.entity.PostEntities;

import com.bhtcnpm.website.model.entity.UserWebsite;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPostReport {
    @EmbeddedId
    private UserPostReportId userPostReportId;

    @OneToMany(
            mappedBy = "postReportReasonId.userPostReport",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Fetch(FetchMode.JOIN)
    private List<PostReportReason> reasons;

    private String feedback;
}
