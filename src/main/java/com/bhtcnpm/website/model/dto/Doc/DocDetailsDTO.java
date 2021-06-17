package com.bhtcnpm.website.model.dto.Doc;

import com.bhtcnpm.website.model.dto.Tag.TagDTO;
import com.bhtcnpm.website.model.validator.dto.Doc.*;
import com.bhtcnpm.website.model.validator.dto.DocCategory.DocCategoryID;
import com.bhtcnpm.website.model.validator.dto.DocCategory.DocCategoryName;
import com.bhtcnpm.website.model.validator.dto.DocSubject.DocSubjectID;
import com.bhtcnpm.website.model.validator.dto.DocSubject.DocSubjectName;
import com.bhtcnpm.website.model.validator.dto.UserWebsite.UserWebsiteDisplayName;
import com.bhtcnpm.website.model.validator.dto.UserWebsite.UserWebsiteName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class DocDetailsDTO {
    @DocID
    private Long id;

    @DocTitle
    private String title;

    @DocDescription
    private String description;

    @DocImageURL
    private String imageURL;

    @DocPublishDtm
    private LocalDateTime publishDtm;

    private UUID authorID;

    @UserWebsiteName
    private String authorName;

    @UserWebsiteDisplayName
    private String authorDisplayName;

    @DocCategoryID
    private Long categoryID;

    @DocCategoryName
    private String categoryName;

    @DocSubjectID
    private Long subjectID;

    @DocSubjectName
    private String subjectName;

    @DocTag
    private Set<TagDTO> tags;
}
