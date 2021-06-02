package com.bhtcnpm.website.model.dto.Post;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class PostSummaryDTO {
    private Long id;
    private String title;
    private String summary;
    private String imageURL;
    private LocalDateTime publishDtm;
    private Integer readingTime;
    private UUID authorID;
    private String authorName;
    private String authorAvatarURL;
    private Long categoryID;
    private String categoryName;
}
