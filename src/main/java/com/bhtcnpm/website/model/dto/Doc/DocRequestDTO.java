package com.bhtcnpm.website.model.dto.Doc;

import com.bhtcnpm.website.model.dto.Tag.TagDTO;
import com.bhtcnpm.website.model.validator.dto.Doc.*;
import com.bhtcnpm.website.model.validator.dto.DocCategory.DocCategoryID;
import com.bhtcnpm.website.model.validator.dto.Subject.SubjectID;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class DocRequestDTO {
    @DocCategoryID
    private Long categoryID;

    @SubjectID
    private Long subjectID;

    @DocTitle
    private String title;

    @DocDescription
    private String description;

    @DocImageURL
    private String imageURL;

    @DocPublishDtm
    private LocalDateTime publishDtm;

    @DocFileUploadRequestDTOList
    private List<DocFileUploadRequestDTO> docFileUploadRequestDTOs;

    @DocTag
    private Set<TagDTO> tags;
}
