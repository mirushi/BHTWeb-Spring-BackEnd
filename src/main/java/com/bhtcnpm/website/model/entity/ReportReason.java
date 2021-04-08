package com.bhtcnpm.website.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "report-reason")
@Data
public class ReportReason {
    private Long id;
    private String reason;
}
