package com.bhtcnpm.website.model.entity.PostEntities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "post_report_reason")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostReportReason {
    @EmbeddedId
    private PostReportReasonId postReportReasonId;
}
