package com.bhtcnpm.website.model.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PostSummaryDTO {
    private Long id;
    private UUID authorID;
    private String authorName;
    private String authorAvatarURL;
    private Long categoryID;
    private String categoryName;
    private String imageURL;
    private LocalDateTime publishDtm;
    private Integer readingTime;
    private String summary;
    private String title;
}
