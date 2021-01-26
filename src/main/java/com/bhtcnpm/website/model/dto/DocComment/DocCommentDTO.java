package com.bhtcnpm.website.model.dto.DocComment;

import lombok.Data;

import java.util.List;

@Data
public class DocCommentDTO {

    private Long id;

    private Long authorID;

    private String authorName;

    private String authorAvatarURL;

    private String content;

    private List<DocCommentDTO> childComments;

}
