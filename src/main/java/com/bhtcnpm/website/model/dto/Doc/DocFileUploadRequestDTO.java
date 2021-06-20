package com.bhtcnpm.website.model.dto.Doc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocFileUploadRequestDTO {
    private UUID id;
    private Integer rank;
}
