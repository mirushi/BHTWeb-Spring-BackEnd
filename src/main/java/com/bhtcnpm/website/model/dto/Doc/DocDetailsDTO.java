package com.bhtcnpm.website.model.dto.Doc;

import com.bhtcnpm.website.model.dto.Tag.TagDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class DocDetailsDTO {
    private Long id;

    private Long authorID;

    private String authorName;

    private String category;

    private Long categoryID;

    private String subject;

    private Long subjectID;

    private String title;

    private String description;

    private String imageURL;

    private Set<TagDTO> tags;

    private LocalDateTime publishDtm;

    private Long downloadCount;

    private Long viewCount;

    private Short version;
}
