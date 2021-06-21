package com.bhtcnpm.website.model.dto.Doc;

public interface DocStatisticDTO {
    Long getId();
    Long getCommentCount();
    Long getLikeCount();
    Long getDislikeCount();
    Long getViewCount();
    Long getDownloadCount();
    Long getDocReactionType();
    Boolean getSavedStatus();
}
