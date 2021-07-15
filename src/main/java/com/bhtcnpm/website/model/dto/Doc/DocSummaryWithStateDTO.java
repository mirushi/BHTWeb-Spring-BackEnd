package com.bhtcnpm.website.model.dto.Doc;

import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocSummaryWithStateDTO {
    //TODO: Unpack this.
    DocSummaryDTO docSummary;
    LocalDateTime submitDtm;
    String feedback;
    DocStateType docState;
}
