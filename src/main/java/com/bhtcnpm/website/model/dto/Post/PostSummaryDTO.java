package com.bhtcnpm.website.model.dto.Post;

import com.bhtcnpm.website.model.validator.dto.Post.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
public class PostSummaryDTO {
    private Long id;
    private String title;
    private String summary;
    private String imageURL;
    private LocalDateTime publishDtm;
    private Integer readingTime;
    private UUID authorID;
    private String authorDisplayName;
    private String authorAvatarURL;
    private Long categoryID;
    private String categoryName;

    public PostSummaryDTO(Long id, String title, String summary, String imageURL, LocalDateTime publishDtm, Integer readingTime, UUID authorID, String authorDisplayName, String authorAvatarURL, Long categoryID, String categoryName) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.imageURL = imageURL;
        this.publishDtm = publishDtm;
        this.readingTime = readingTime;
        this.authorID = authorID;
        this.authorDisplayName = authorDisplayName;
        this.authorAvatarURL = authorAvatarURL;
        this.categoryID = categoryID;
        this.categoryName = categoryName;
    }
}
