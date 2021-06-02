package com.bhtcnpm.website.model.dto.PostComment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PostCommentChildDTO {
    private Long id;
    private UUID authorID;
    private String authorAvatarURL;
    private String content;
}
