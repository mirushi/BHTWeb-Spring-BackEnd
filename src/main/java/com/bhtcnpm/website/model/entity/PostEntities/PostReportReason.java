package com.bhtcnpm.website.model.entity.PostEntities;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "post_report_reason")
@Data
public class PostReportReason {
    @EmbeddedId
    private PostReportReasonId postReportReasonId;
}
