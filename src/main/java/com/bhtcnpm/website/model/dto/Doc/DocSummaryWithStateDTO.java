package com.bhtcnpm.website.model.dto.Doc;

import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import lombok.Data;

@Data
public class DocSummaryWithStateDTO {
    DocSummaryDTO docSummary;
    DocStateType docState;
}
