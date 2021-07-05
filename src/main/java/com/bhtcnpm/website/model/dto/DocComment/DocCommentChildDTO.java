package com.bhtcnpm.website.model.dto.DocComment;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class DocCommentChildDTO {
    Long id;
    UUID authorID;
    String authorDisplayName;
    String authorAvatarURL;
    LocalDateTime submitDtm;
    LocalDateTime lastEditedDtm;
    String content;
}
