package com.bhtcnpm.website.model.dto.Doc;

import com.bhtcnpm.website.model.entity.enumeration.DocReaction.DocReactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class DocStatisticDTOImpl {
    private Long id;
    private Long commentCount;
    private Long likeCount;
    private Long dislikeCount;
    private Long viewCount;
    private Long downloadCount;
    private DocReactionType docReactionType;
    private Boolean savedStatus;

    public DocStatisticDTOImpl(Long id, Long commentCount, Long likeCount, Long dislikeCount,
                               Long viewCount, Long downloadCount, DocReactionType docReactionType,
                               Boolean savedStatus) {
        this.id = id;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.viewCount = viewCount;
        this.downloadCount = downloadCount;
        this.docReactionType = docReactionType;
        this.savedStatus = savedStatus;
    }
}
