package com.bhtcnpm.website.model.entity.ReportReason;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "report_reason")
@Data
public class ReportReason {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "report_reason_sequence"
    )
    @SequenceGenerator(
            name = "report_reason_sequence",
            sequenceName = "report_reason_sequence"
    )
    private Long id;

    @Column(name = "reason", updatable = false)
    private String reason;
}
