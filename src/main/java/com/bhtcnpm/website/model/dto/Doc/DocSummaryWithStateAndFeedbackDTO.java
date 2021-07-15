package com.bhtcnpm.website.model.dto.Doc;

import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.bhtcnpm.website.model.validator.dto.Doc.*;
import com.bhtcnpm.website.model.validator.dto.DocCategory.DocCategoryID;
import com.bhtcnpm.website.model.validator.dto.DocCategory.DocCategoryName;
import com.bhtcnpm.website.model.validator.dto.Subject.SubjectID;
import com.bhtcnpm.website.model.validator.dto.Subject.SubjectName;
import com.bhtcnpm.website.model.validator.dto.UserWebsite.UserWebsiteDisplayName;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class DocSummaryWithStateAndFeedbackDTO {
    @DocID
    Long id;

    UUID authorID;

    @UserWebsiteDisplayName
    String authorDisplayName;

    String authorAvatarURL;

    @DocCategoryID
    Long categoryID;

    @DocCategoryName
    String categoryName;

    @SubjectID
    Long subjectID;

    @SubjectName
    String subjectName;

    @DocTitle
    String title;

    @DocDescription
    String description;

    @DocImageURL
    String imageURL;

    LocalDateTime submitDtm;

    @DocPublishDtm
    LocalDateTime publishDtm;

    DocStateType docState;

    String feedback;
}
