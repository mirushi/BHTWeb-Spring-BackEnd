package com.bhtcnpm.website.model.dto.ExerciseComment;

import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseCommentDTO {
    private Long id;
    private UUID authorID;
    private String authorDisplayName;
    private String authorAvatarURL;
    private LocalDateTime submitDtm;
    private LocalDateTime lastEditedDtm;
    private String content;
    private Long childCommentCount;

    public ExerciseCommentDTO(ExerciseComment exerciseComment, Long childCommentCount){
        this.id = exerciseComment.getId();
        this.authorID = exerciseComment.getAuthor().getId();
        this.authorDisplayName = exerciseComment.getAuthor().getDisplayName();
        this.authorAvatarURL = exerciseComment.getAuthor().getAvatarURL();
        this.submitDtm = exerciseComment.getSubmitDtm();
        this.lastEditedDtm = exerciseComment.getLastEditedDtm();
        this.content = exerciseComment.getContent();
        this.childCommentCount = childCommentCount;
    }
}
