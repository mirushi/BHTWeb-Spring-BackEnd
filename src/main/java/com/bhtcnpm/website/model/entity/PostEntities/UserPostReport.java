package com.bhtcnpm.website.model.entity.PostEntities;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
@Data
public class UserPostReport {
    @EmbeddedId
    private UserPostReportId userPostReportId;
}
