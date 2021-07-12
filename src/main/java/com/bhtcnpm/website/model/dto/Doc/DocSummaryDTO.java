package com.bhtcnpm.website.model.dto.Doc;

import com.bhtcnpm.website.model.validator.dto.Doc.*;
import com.bhtcnpm.website.model.validator.dto.DocCategory.DocCategoryID;
import com.bhtcnpm.website.model.validator.dto.DocCategory.DocCategoryName;
import com.bhtcnpm.website.model.validator.dto.Subject.SubjectID;
import com.bhtcnpm.website.model.validator.dto.Subject.SubjectName;
import com.bhtcnpm.website.model.validator.dto.UserWebsite.UserWebsiteDisplayName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class DocSummaryDTO {
    @DocID
    private Long id;

    private UUID authorID;

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

    @DocTitle
    private String title;

    @DocDescription
    private String description;

    @DocImageURL
    private String imageURL;

    @DocPublishDtm
    private LocalDateTime publishDtm;
}
