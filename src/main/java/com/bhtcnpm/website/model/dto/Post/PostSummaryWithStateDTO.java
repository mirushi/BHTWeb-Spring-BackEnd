package com.bhtcnpm.website.model.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostSummaryWithStateDTO {
    private Long id;
    private Long authorID;
    private String authorName;
    private Long categoryID;
    private String categoryName;
    private String imageURL;
    private LocalDateTime publishDtm;
    private Integer readingTime;
    private String summary;
    private String title;
    private PostStateType postState;
}
