package com.bhtcnpm.website.model.entity.ReportReason;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class ReportReasonFeedback {
    @EmbeddedId
    private ReportReasonFeedbackId reportReasonFeedbackId;
    private String feedback;
}
