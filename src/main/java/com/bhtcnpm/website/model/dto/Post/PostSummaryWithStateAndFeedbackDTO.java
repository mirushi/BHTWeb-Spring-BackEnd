package com.bhtcnpm.website.model.dto.Post;

import com.bhtcnpm.website.constant.domain.Post.PostBusinessState;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PostSummaryWithStateAndFeedbackDTO {
    private Long id;
    private String title;
    private String summary;
    private String imageURL;
    private LocalDateTime submitDtm;
    private LocalDateTime publishDtm;
    private Integer readingTime;
    private UUID authorID;
    private String authorDisplayName;
    private String authorAvatarURL;
    private Long categoryID;
    private String categoryName;
    private String feedback;
    private PostStateType postState;
    private PostBusinessState postBusinessState;
}
