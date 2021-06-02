package com.bhtcnpm.website.model.dto.ReportReason;

import com.bhtcnpm.website.model.entity.DocEntities.UserDocReactionId;
import lombok.Data;

import java.util.Objects;

@Data
public class ReportReasonDTO {
    private Long id;
    private String reason;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportReasonDTO)) return false;
        ReportReasonDTO that = (ReportReasonDTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
