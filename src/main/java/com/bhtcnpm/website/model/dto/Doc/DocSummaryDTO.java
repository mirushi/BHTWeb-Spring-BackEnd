package com.bhtcnpm.website.model.dto.Doc;

import com.bhtcnpm.website.model.dto.Tag.TagDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
public class DocSummaryDTO {
    private Long id;

    private Long authorID;

    private String authorName;

    private Long categoryID;

    private String category;

    private Long subjectID;

    private String subject;

    private String title;

    private String description;

    private String imageURL;

    private LocalDateTime publishDtm;

    private Long downloadCount;

    private Long viewCount;

    private Short version;
}
