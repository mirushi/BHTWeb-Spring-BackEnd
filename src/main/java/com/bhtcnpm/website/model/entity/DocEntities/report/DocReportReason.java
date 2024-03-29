package com.bhtcnpm.website.model.entity.DocEntities.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "doc_report_reason")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocReportReason {
    @EmbeddedId
    private DocReportReasonId docReportReasonId;
}
