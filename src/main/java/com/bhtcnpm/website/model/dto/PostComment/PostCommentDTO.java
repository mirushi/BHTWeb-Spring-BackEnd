package com.bhtcnpm.website.model.dto.PostComment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PostCommentDTO {
    private Long id;
    private UUID authorID;
    private String avatarURL;
    private String content;
    private Long childCommentCount;
}
