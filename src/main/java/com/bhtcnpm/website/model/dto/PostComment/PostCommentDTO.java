package com.bhtcnpm.website.model.dto.PostComment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostCommentDTO {
    private Long id;
    private Long authorID;
    private String avatarURL;
    private String content;
    private Long childCommentCount;
}
