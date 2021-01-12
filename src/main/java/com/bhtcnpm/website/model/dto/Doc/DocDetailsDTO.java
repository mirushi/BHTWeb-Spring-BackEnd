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

    private String categoryID;

    private String docSubject;

    private Long docSubjectID;

    private String title;

    private String description;

    private String imageURL;

    private String docURL;

    private Set<TagDTO> tags;

    private LocalDateTime publishDtm;

    private Long downloads;

    private Long views;
}
