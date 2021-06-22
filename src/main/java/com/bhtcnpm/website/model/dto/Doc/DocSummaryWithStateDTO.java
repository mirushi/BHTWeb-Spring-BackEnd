package com.bhtcnpm.website.model.dto.Doc;

import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import lombok.Data;

@Data
public class DocSummaryWithStateDTO {
    //TODO: Unpack this.
    DocSummaryDTO docSummary;
    DocStateType docState;
}
