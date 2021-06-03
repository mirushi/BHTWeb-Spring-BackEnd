package com.bhtcnpm.website.model.dto.PostComment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PostCommentChildDTO {
    private Long id;
    private UUID authorID;
    private String authorDisplayName;
    private String authorAvatarURL;
    private LocalDateTime submitDtm;
    private LocalDateTime lastEditedDtm;
    private String content;
}
