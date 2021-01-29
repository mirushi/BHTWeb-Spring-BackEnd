package com.bhtcnpm.website.model.dto.Post;

import com.bhtcnpm.website.model.dto.Tag.TagDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class PostDetailsDTO {
    private Long id;
    private String title;
    private String summary;
    private String imageURL;
    private LocalDateTime publishDtm;
    private Long readingTime;
    private String content;
    private Set<TagDTO> tags;
    private Long authorID;
    private String authorName;
    private Long categoryID;
    private String categoryName;
}