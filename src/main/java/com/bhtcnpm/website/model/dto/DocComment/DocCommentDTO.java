package com.bhtcnpm.website.model.dto.DocComment;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class DocCommentDTO {

    private Long id;

    private UUID authorID;

    private String authorName;

    private String authorAvatarURL;

    private String content;

    private List<DocCommentDTO> childComments;

}
