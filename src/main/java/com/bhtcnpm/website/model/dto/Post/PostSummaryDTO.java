package com.bhtcnpm.website.model.dto.Post;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostSummaryDTO {
    private Long id;
    private Long authorID;
    private String authorName;
    private Long categoryID;
    private String categoryName;
    private String imageURL;
    private LocalDateTime publishDtm;
    private Long readingTime;
    private String summary;
    private String title;
}
