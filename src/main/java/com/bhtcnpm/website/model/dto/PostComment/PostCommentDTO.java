package com.bhtcnpm.website.model.dto.PostComment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentDTO {
    private Long id;
    private UUID authorID;
    private String authorAvatarURL;
    private String content;
    private Long childCommentCount;
}
