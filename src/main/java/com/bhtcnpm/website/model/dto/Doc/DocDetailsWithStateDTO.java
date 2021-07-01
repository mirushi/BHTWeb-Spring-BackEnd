package com.bhtcnpm.website.model.dto.Doc;

import com.bhtcnpm.website.model.dto.Tag.TagDTO;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
public class DocDetailsWithStateDTO {
    private final Long id;
    private String title;
    private String description;
    private String imageURL;
    private LocalDateTime submitDtm;
    private LocalDateTime publishDtm;
    private UUID authorID;
    private String authorName;
    private String authorDisplayName;
    private Long categoryID;
    private String categoryName;
    private Long subjectID;
    private String subjectName;
    private List<DocFileUploadDTO> docFileUploadDTOs;
    private Set<TagDTO> tags;
    private DocStateType docState;
}
