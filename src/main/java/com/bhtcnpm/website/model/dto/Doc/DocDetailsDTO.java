package com.bhtcnpm.website.model.dto.Doc;

import com.bhtcnpm.website.model.dto.Tag.TagDTO;
import com.bhtcnpm.website.model.validator.dto.Doc.*;
import com.bhtcnpm.website.model.validator.dto.DocCategory.DocCategoryID;
import com.bhtcnpm.website.model.validator.dto.DocCategory.DocCategoryName;
import com.bhtcnpm.website.model.validator.dto.Subject.SubjectID;
import com.bhtcnpm.website.model.validator.dto.Subject.SubjectName;
import com.bhtcnpm.website.model.validator.dto.UserWebsite.UserWebsiteDisplayName;
import com.bhtcnpm.website.model.validator.dto.UserWebsite.UserWebsiteName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
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

    private String authorAvatarURL;

    @DocCategoryID
    private Long categoryID;

    @DocCategoryName
    private String categoryName;

    @SubjectID
    private Long subjectID;

    @SubjectName
    private String subjectName;

    private List<DocFileUploadDTO> docFileUploadDTOs;

    @DocTag
    private Set<TagDTO> tags;
}
