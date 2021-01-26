package com.bhtcnpm.website.model.dto.Post;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDTO {
    private Long id;
    private Long authorID;
    private String authorName;
    private Long categoryID;
    private String categoryName;
    private Long commentCount;
    private String imageURL;
    private Long likeCount;
    private Boolean likeStatus;
    private LocalDateTime publishDtm;
    private Long readingTime;
    private String summary;
    private String title;
}
