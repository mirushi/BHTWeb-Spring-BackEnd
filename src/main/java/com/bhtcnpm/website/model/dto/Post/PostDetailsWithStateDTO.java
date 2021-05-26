package com.bhtcnpm.website.model.dto.Post;

import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PostDetailsWithStateDTO {
    private Long id;
    private String title;
    private String imageURL;
    private LocalDateTime publishDtm;
    private Integer readingTime;
    private String content;
    private UUID authorID;
    private String authorName;
    private String authorAvatarURL;
    private Long categoryID;
    private String categoryName;
    private PostStateType postState;
}
