package com.bhtcnpm.website.model.dto.Post;

import com.bhtcnpm.website.model.dto.Tag.TagDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class PostRequestDTO {
    private String title;
    private String content;
    private String summary;
    private String imageURL;
    private Long categoryID;
    private Set<TagDTO> tags;
}
