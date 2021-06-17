package com.bhtcnpm.website.model.dto.Doc;

import com.bhtcnpm.website.model.dto.Tag.TagDTO;
import com.bhtcnpm.website.model.validator.dto.Doc.DocID;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class DocSummaryDTO {
    @DocID
    private Long id;

    private UUID authorID;

    private String authorDisplayName;

    private Long categoryID;

    private String categoryName;

    private Long subjectID;

    private String subjectName;

    private String title;

    private String description;

    private String imageURL;

    private LocalDateTime publishDtm;
}
