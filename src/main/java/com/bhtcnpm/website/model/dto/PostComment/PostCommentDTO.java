package com.bhtcnpm.website.model.dto.PostComment;

import com.bhtcnpm.website.model.entity.PostCommentEntities.PostComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentDTO {
    private Long id;
    private UUID authorID;
    private String authorDisplayName;
    private String authorAvatarURL;
    private LocalDateTime submitDtm;
    private LocalDateTime lastEditedDtm;
    private String content;
    private Long childCommentCount;

    public PostCommentDTO (PostComment postComment, Long childCommentCount) {
        this.id = postComment.getId();
        this.authorID = postComment.getAuthor().getId();
        this.authorDisplayName = postComment.getAuthor().getDisplayName();
        this.authorAvatarURL = postComment.getAuthor().getAvatarURL();
        this.submitDtm = postComment.getSubmitDtm();
        this.lastEditedDtm = postComment.getLastEditedDtm();
        this.content = postComment.getContent();
        this.childCommentCount = childCommentCount;
    }
}
