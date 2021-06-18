package com.bhtcnpm.website.model.dto.Doc;

import com.bhtcnpm.website.model.dto.Tag.TagDTO;
import com.bhtcnpm.website.model.validator.dto.Doc.*;
import com.bhtcnpm.website.model.validator.dto.DocCategory.DocCategoryID;
import com.bhtcnpm.website.model.validator.dto.DocSubject.DocSubjectID;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class DocRequestDTO {
    @DocCategoryID
    private Long categoryID;

    @DocSubjectID
    private Long subjectID;

    @DocTitle
    private String title;

    @DocDescription
    private String description;

    @DocImageURL
    private String imageURL;

    private UUID fileCode;

    @DocTag
    private Set<TagDTO> tags;

    @DocPublishDtm
    private LocalDateTime publishDtm;
}
