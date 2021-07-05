package com.bhtcnpm.website.model.dto.DocComment;

import com.bhtcnpm.website.model.entity.DocCommentEntities.DocComment;
import lombok.Data;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DocCommentDTO {
    private Long id;
    private UUID authorID;
    private String authorDisplayName;
    private String authorAvatarURL;
    private LocalDateTime submitDtm;
    private LocalDateTime lastEditedDtm;
    private String content;
    private Long childCommentCount;

    public DocCommentDTO (DocComment docComment, Long childCommentCount) {
        this.id = docComment.getId();
        this.authorID = docComment.getAuthor().getId();
        this.authorDisplayName = docComment.getAuthor().getDisplayName();
        this.authorAvatarURL = docComment.getAuthor().getAvatarURL();
        this.submitDtm = docComment.getSubmitDtm();
        this.lastEditedDtm = docComment.getLastEditedDtm();
        this.content = docComment.getContent();
        this.childCommentCount = childCommentCount;
    }
}
