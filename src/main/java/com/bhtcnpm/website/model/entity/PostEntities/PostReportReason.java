package com.bhtcnpm.website.model.entity.PostEntities;

import com.bhtcnpm.website.model.entity.ReportReason.ReportReason;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "post_report_reason")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostReportReason {
    @EmbeddedId
    private PostReportReasonId postReportReasonId;
}
