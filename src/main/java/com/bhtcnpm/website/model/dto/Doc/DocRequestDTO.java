package com.bhtcnpm.website.model.dto.Doc;

import com.bhtcnpm.website.model.dto.Tag.TagDTO;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class DocRequestDTO {
    private Long categoryID;

    private Long subjectID;

    private String title;

    private String description;

    private String imageURL;

    private String docURL;

    private Set<TagDTO> tags;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime publishDtm;

    private short version;
}
